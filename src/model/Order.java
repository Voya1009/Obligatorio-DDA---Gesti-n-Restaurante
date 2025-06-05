package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import system.*;

public class Order {

    private Item item;
    private String comment;
    private OrderState state;
    private ProcessingUnit assignedPU;
    private Manager manager;
    private LocalDateTime confirmedAt;

    public Order(Item item, String comment) {
        this.item = item;
        this.comment = comment;
        this.state = OrderState.NOT_CONFIRMED;
        this.manager = null;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OrderState getState() {
        return state;
    }

    public ProcessingUnit getAssignedUP() {
        return assignedPU;
    }

    public void setAssignedUP(ProcessingUnit assignedUP) {
        this.assignedPU = assignedUP;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public LocalDateTime getDate() {
        return confirmedAt;
    }

    public boolean isInProgress() {
        return state == OrderState.IN_PROGRESS;
    }

    public boolean isReadyOrDelivered() {
        return state == OrderState.READY || state == OrderState.DELIVERED;
    }

    public boolean isCancelable() {
        if (state == OrderState.NOT_CONFIRMED || state == OrderState.CONFIRMED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result = item.getName() + " - $" + item.getPrice();
        result += " | Estado: " + state;
        if (state != OrderState.NOT_CONFIRMED) {
            result += " | Unidad: " + item.getPU().getName();
            if (manager != null) {
                result += " | Gestor: " + manager.getName();
            } else {
                result += " | Esperando gestor";
            }
            if (state != OrderState.NOT_CONFIRMED && confirmedAt != null) {
                result += " | Confirmado el: " + confirmedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
        }
        return result;
    }

    public void changeState(OrderState newState) throws SystemException {
        if (state == OrderState.NOT_CONFIRMED && newState == OrderState.CONFIRMED) {
            state = newState;
        } else if (state == OrderState.CONFIRMED && newState == OrderState.IN_PROGRESS) {
            state = newState;
        } else if (state == OrderState.IN_PROGRESS && newState == OrderState.READY) {
            state = newState;
        } else if (state == OrderState.READY && newState == OrderState.DELIVERED) {
            state = newState;
        } else {
            throw new SystemException("No se puede cambiar el estado de " + state + " a " + newState + ".");
        }
    }

    public void advanceState(Manager manager) throws SystemException {
        this.validate();
        switch (state) {
            case CONFIRMED:
                setManager(manager);
                changeState(OrderState.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                changeState(OrderState.READY);
                break;
            case READY:
                changeState(OrderState.DELIVERED);
                break;
            default:
                throw new SystemException("No se puede avanzar el estado del pedido.");
        }
    }

    public void validate() throws SystemException {
        if (this == null) {
            throw new SystemException("Debe seleccionar un pedido.");
        }
    }

    public void validateStock(SupplyManager supplyManager) throws SystemException {
        for (Ingredient ing : this.getItem().getIngredients()) {
            Supply s = ing.getSupply();
            s.decreaseStock(ing.getQuantity());
            if (s.getStock() <= s.getMinStock()) {
                supplyManager.handleOutOfStock(s);                
            }
        }
    }
}
