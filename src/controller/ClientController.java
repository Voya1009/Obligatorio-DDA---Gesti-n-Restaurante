package controller;

import model.*;
import observer.Observer;
import system.SystemException;
import system.SystemFacade;
import view.ClientView;
import java.util.List;

import javax.swing.JOptionPane;

public class ClientController implements Observer {

    private final SystemFacade system;
    private final ClientView view;
    private Client client;
    private Device device;

    public ClientController(SystemFacade system, Device device) {
        this.system = system;
        this.device = device;
        this.view = new ClientView(this);
        this.system.addObserver(this);
        this.view.updateItemList(system.getMenuItems());
    }

    public void handleLogin() {
        String username = view.getUsernameField();
        String password = view.getPasswordField();
        try {
            if (!device.isAvailable()) {
                throw new SystemException("El dispositivo esta siendo usado por otra persona.");
            }
            Client c = system.getClient(username, password);
            if (c.getService() != null) {
                throw new SystemException("El cliente ya esta utilizando otro dispositivo.");
            } else {
                client = c;
                device.assignClient(client);
                client.newService();
            }
            view.setClientName(client.getName());
            view.showMessage("Inicio de sesión exitoso.");
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void handleAddOrder() {
        try {
            if (client == null) {
                throw new SystemException("Debe identificarse antes de realizar pedidos.");
            }
            List<Item> selectedItems = view.getSelectedItems();
            if (selectedItems.isEmpty()) {
                throw new SystemException("Debe seleccionar al menos un ítem.");
            }
            String comment = view.getCommentField();
            for (Item item : selectedItems) {
                client.getService().addOrder(item, comment);
            }
            view.clearOrderInputs();
            update();
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void handleConfirmOrders() {
        try {
            if (client == null) {
                throw new SystemException("Debe identificarse antes de confirmar pedidos.");
            }
            List<Order> selectedOrders = view.getSelectedOrders();
            client.getService().confirmOrders(system, selectedOrders);
            view.showMessage("Pedidos confirmados.");
            update();
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void handleCancelOrders() {
        try {
            if (client == null) {
                throw new SystemException("Debe identificarse antes de cancelar pedidos.");
            }
            List<Order> selected = view.getSelectedOrders();
            client.getService().cancelOrders(selected);
            if (selected.size() == 1) {
                view.showMessage("Pedido cancelado.");
            } else {
                view.showMessage("Pedidos cancelados.");
            }
            update();
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void handleFinalizeService() {
        try {
            if (client == null) {
                throw new SystemException("Debe identificarse antes de finalizar un servicio.");
            }
            double[] totals = client.getService().tryFinalize(client.getPaymentPolicy());
            if (totals != null) {
                double total = totals[0];
                double benefit = totals[1];
                view.showPaymentDetails(total, benefit);
            }
            view.showMessage("Servicio finalizado.");
            finalizeAndCleanup();
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void finalizeAndCleanup() {
        client.endService();
        device.releaseClient();
        device = null;
        view.clearServiceData();
        client = null;
    }

    @Override
    public void update() {
        if (client != null) {
            Service s = client.getService();
            if (s != null) {
                if (s.hasRemovedItemsByStock()) {
                    List<Item> removed = s.getRemovedItemsByStock();
                    if (removed.size() == 1) view.showMessage("Se ha removido un pedido por falta de stock. Lamentamos no haberle podido avisar antes.");
                    else view.showMessage("Se han removido algunos pedidos por falta de stock. Lamentamos no haberle podido avisar antes.");
                }
                view.updateOrderList(s.getOrders());
                view.updateServiceTotal(s.calculateFinalTotal(client.getPaymentPolicy()));
            }
        } else {
            view.clearOrderList();
        }
    }
}
