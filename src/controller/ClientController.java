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
    private Client activeClient;
    private Device activeDevice;

    public ClientController(SystemFacade system, Device device) {
        this.system = system;
        this.activeDevice = device;
        this.view = new ClientView(this);
        this.system.addObserver(this);
        this.view.updateItemList(system.getMenuItems());
    }

    public void handleLogin() {
        String username = view.getUsernameField();
        String password = view.getPasswordField();
        try {
            if (!activeDevice.isAvailable()) {
                throw new SystemException("El dispositivo esta siendo usado por otra persona.");
            }
            Client c = system.getClient(username, password);
            if (c.getService() != null) {
                throw new SystemException("El cliente ya esta utilizando otro dispositivo.");
            } else {
                activeClient = c;
                activeDevice.assignClient(activeClient);
                activeClient.newService();
            }
            view.setClientName(activeClient.getName());
            view.showMessage("Inicio de sesión exitoso.");
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void handleAddOrder() {
        try {
            if (activeClient == null) {
                throw new SystemException("Debe identificarse antes de realizar pedidos.");
            }
            List<Item> selectedItems = view.getSelectedItems();
            if (selectedItems.isEmpty()) {
                throw new SystemException("Debe seleccionar al menos un ítem.");
            }
            String comment = view.getCommentField();
            for (Item item : selectedItems) {
                Order order = new Order(item, comment);
                activeClient.getService().addOrder(item, comment);
            }
            view.clearOrderInputs();
            update();
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void handleConfirmOrders() {
        try {
            if (activeClient == null) {
                throw new SystemException("Debe identificarse antes de confirmar pedidos.");
            }
            List<Order> selectedOrders = view.getSelectedOrders();
            Service s = activeClient.getService();
            s.confirmOrders(system, selectedOrders);
            view.showMessage("Pedidos confirmados.");
            update();
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void handleCancelOrders() {
        try {
            if (activeClient == null) {
                throw new SystemException("Debe identificarse antes de cancelar pedidos.");
            }
            List<Order> selected = view.getSelectedOrders();
            Service s = activeClient.getService();
            s.cancelOrders(selected);
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
            if (activeClient == null) {
                throw new SystemException("Debe identificarse antes de finalizar un servicio.");
            }
            double[] totals = activeClient.getService().tryFinalize(activeClient.getPaymentPolicy());
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
        activeClient.endService();
        activeDevice.releaseClient();
        activeDevice = null;
        view.clearServiceData();
        activeClient = null;
    }

    @Override
    public void update() {
        if (activeClient != null) {
            Service s = activeClient.getService();
            if (s != null) {
                view.updateOrderList(s.getOrders());
                view.updateServiceTotal(s.calculateFinalTotal(activeClient.getPaymentPolicy()));
            }
        } else {
            view.clearOrderList();
        }
    }
}
