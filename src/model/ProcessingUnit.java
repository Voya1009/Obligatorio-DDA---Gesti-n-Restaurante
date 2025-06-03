package model;

import java.util.ArrayList;

public class ProcessingUnit {

    private String name;
    private ArrayList<Order> queue;

    public ProcessingUnit(String name) {
        this.name = name;
        this.queue = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void enqueueOrder(Order order) {
        if (!queue.contains(order)) {
            queue.add(order);
        }
    }

    public ArrayList<Order> getPendingOrders() {
        ArrayList<Order> pending = new ArrayList<>();
        for (Order o : queue) {
            if (o.getState() == OrderState.CONFIRMED) {
                pending.add(o);
            }
        }
        return pending;
    }

    public void removeOrder(Order order) {
        queue.remove(order);
    }
}
