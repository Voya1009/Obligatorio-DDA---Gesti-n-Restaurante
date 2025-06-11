package controller;

import model.*;
import observer.Observer;
import system.*;
import view.ManagerView;

import java.util.List;

public class ManagerController implements Observer {

    private final SystemFacade system;
    private final Manager manager;

    private final ManagerView view;

    public ManagerController(SystemFacade system, Manager manager) {
        this.system = system;
        this.manager = manager;
        this.view = new ManagerView(this);
        system.addObserver(this);
        update();
    }

    public String getManagerName() {
        return manager.getName();
    }

    public void handleTakeOrder(List<Order> orders) {
        handleOrderAction(orders, OrderActions.TAKE);
    }

    public void handleReadyOrder(List<Order> orders) {
        handleOrderAction(orders, OrderActions.READY);
    }

    public void handleDeliverOrder(List<Order> orders) {
        handleOrderAction(orders, OrderActions.DELIVER);
    }

    private void handleOrderAction(List<Order> orders, OrderAction action) {
        try {
            if (orders == null || orders.isEmpty()) {
                view.showMessage("Debe seleccionar al menos un pedido.");
                return;
            }
            for (Order order : orders) {
                action.apply(order, manager);
            }
            if (orders.size() == 1) {
                view.showMessage("Pedido actualizado correctamente.");
            } else {
                view.showMessage("Pedidos actualizados correctamente.");
            }
            system.notifyObservers();
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    public boolean hasUndeliveredOrders() {
        return !system.getUserManager().getOrdersByManager(manager, OrderState.IN_PROGRESS, OrderState.READY).isEmpty();
    }

    @Override
    public void update() {
        List<Order> pending = system.getOrdersByUnitAndState(manager.getPU(), OrderState.CONFIRMED);
        List<Order> taken = system.getOrdersByManager(manager, OrderState.IN_PROGRESS, OrderState.READY);
        view.updatePendingList(pending);
        view.updateTakenList(taken);
    }
}
