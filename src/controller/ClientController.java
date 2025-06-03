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
            Item item = view.getSelectedItem();
            String comment = view.getCommentField();
            activeClient.getService().addOrder(item, comment);
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

    public void handleFinalizeService() {
        try {
            if (activeClient == null) {
                throw new SystemException("Debe identificarse antes de finalizar un servicio.");
            }
            Service service = activeClient.getService();
            String paymentDetail = service.tryFinalize(activeClient.getPaymentPolicy());

            if (paymentDetail != null) {
                JOptionPane.showMessageDialog(
                        view,
                        paymentDetail,
                        "Detalle de pago",
                        JOptionPane.INFORMATION_MESSAGE
                );
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
