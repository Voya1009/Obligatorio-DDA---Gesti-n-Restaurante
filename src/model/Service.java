package model;

import java.util.ArrayList;
import java.util.List;
import system.*;
import java.time.LocalDateTime;

public class Service {

    private ArrayList<Order> orders;
    private boolean isFinalized;
    private List<Item> removedItemsByStock = new ArrayList<>();

    public Service() {
        this.orders = new ArrayList<>();
        this.isFinalized = false;
    }

    public void addOrder(Item item, String comment, Client client) throws SystemException {
        if (item == null) {
            throw new SystemException("Debe seleccionar un ítem.");
        }
        if (isFinalized) {
            throw new SystemException("El servicio ya fue finalizado.");
        }
        orders.add(new Order(item, comment, client));
    }

    public void removeOrder(Order o) throws SystemException {
        if (o.getState() == OrderState.IN_PROGRESS) {
            throw new SystemException("Pedido en proceso.");
        }
        if (o.getState() == OrderState.READY) {
            throw new SystemException("El pedido esta listo.");
        }
        if (o.getState() == OrderState.DELIVERED) {
            throw new SystemException("El pedido ya fue entregado.");
        }
        orders.remove(o);
    }

    public ArrayList<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public void endService() {
        this.isFinalized = true;
    }

    public boolean isFinalized() {
        return isFinalized;
    }

    public double calculateRawTotal() {
        double total = 0;
        for (Order order : orders) {
            total += order.getItem().getPrice();
        }
        return total;
    }

    public double calculateFinalTotal(PaymentPolicy policy) {
        return policy.calculateTotal(calculateRawTotal(), orders);
    }

    public void confirmOrders(SystemFacade system, List<Order> selected) throws SystemException {
        if (selected == null || selected.isEmpty()) {
            throw new SystemException("Debe seleccionar al menos un pedido para confirmar.");
        }
        List<Order> toConfirm = new ArrayList<>();
        for (Order o : selected) {
            if (o.getState() == OrderState.NOT_CONFIRMED) {
                toConfirm.add(o);
            }
        }
        if (toConfirm.isEmpty()) {
            throw new SystemException("Debe seleccionar pedidos sin confirmar.");
        }
        boolean anyConfirmed = false;
        for (Order o : toConfirm) {
            o.validateStock(system.getSupplyManager());
            o.changeState(OrderState.CONFIRMED);
            o.setConfirmationDate(LocalDateTime.now());
            system.submitOrderToUnit(o);
            anyConfirmed = true;
        }
        if (!anyConfirmed) {
            throw new SystemException("No se pudo confirmar ningún pedido.");
        }
    }

    public void cancelOrders(List<Order> selected) throws SystemException {
        if (selected == null || selected.isEmpty()) {
            throw new SystemException("Debe seleccionar al menos un pedido para cancelar.");
        }
        List<Order> nonCancelable = new ArrayList<>();
        List<Order> cancelable = new ArrayList<>();
        for (Order o : selected) {
            if (o.isCancelable()) {
                cancelable.add(o);
            } else {
                nonCancelable.add(o);
            }
        }
        orders.removeAll(cancelable);
        if (!nonCancelable.isEmpty()) {
            throw new SystemException("Algunos pedidos no se pudieron cancelar:");
        }
    }

    public void removeUnconfirmedOrdersByItem(Item item) {
        boolean removed = orders.removeIf(o -> o.getState() == OrderState.NOT_CONFIRMED && o.getItem().equals(item));
        if (removed) {
            removedItemsByStock.add(item);
        }
    }

    public boolean hasRemovedItemsByStock() {
        return !removedItemsByStock.isEmpty();
    }

    public List<Item> getRemovedItemsByStock() {
        List<Item> removed = new ArrayList<>(removedItemsByStock);
        removedItemsByStock.clear();
        return removed;
    }

    public double[] tryFinalize(PaymentPolicy policy) throws SystemException {
        if (orders.isEmpty()) {
            endService();
            return null;
        }
        boolean hasInProgress = orders.stream().anyMatch(Order::isInProgress);
        if (hasInProgress) {
            throw new SystemException("No puede finalizar el servicio: hay pedidos en preparación.");
        }
        boolean hasReadyOrDelivered = orders.stream().anyMatch(Order::isReadyOrDelivered);
        endService();
        if (hasReadyOrDelivered) {
            double rawTotal = calculateRawTotal();
            double finalTotal = calculateFinalTotal(policy);
            double benefit = rawTotal - finalTotal;
            return new double[]{finalTotal, benefit};
        }
        return null;
    }
}
