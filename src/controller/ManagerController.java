package controller;

import model.*;
import observer.Observer;
import system.SystemException;
import system.SystemFacade;
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

    public void handleTakeOrder(List<Order> orders) {
        handleAdvanceOrders(orders);
    }

    public void handleReadyOrder(List<Order> orders) {
        handleAdvanceOrders(orders);
    }

    public void handleDeliverOrder(List<Order> orders) {
        handleAdvanceOrders(orders);
    }

    public void handleAdvanceOrders(List<Order> orders) {
        try {
            if (orders == null || orders.isEmpty()) {
                view.showMessage("Debe seleccionar al menos un pedido.");
                return;
            }
            for (Order o : orders) { o.advanceState(manager); }
            if (orders.size() == 1) { view.showMessage("Pedido actualizado correctamente."); } 
            else { view.showMessage("Pedidos actualizados correctamente."); }
            system.notifyObservers();
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void update() {
        List<Order> pending = system.getOrdersByUnitAndState(manager.getPU(), OrderState.CONFIRMED);
        List<Order> taken = system.getOrdersByManager(manager, OrderState.IN_PROGRESS, OrderState.READY);
        view.updatePendingList(pending);
        view.updateTakenList(taken);
    }
}
