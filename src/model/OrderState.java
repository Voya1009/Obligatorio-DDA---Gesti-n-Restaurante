package model;

public enum OrderState {
    NOT_CONFIRMED,
    CONFIRMED,
    IN_PROGRESS,
    READY,
    DELIVERED;

    @Override
    public String toString() {
        return switch (this) {
            case NOT_CONFIRMED -> "Sin confirmar";
            case CONFIRMED -> "Confirmado";
            case IN_PROGRESS -> "En proceso";
            case READY -> "Listo";
            case DELIVERED -> "Entregado";
        };
    }
}
