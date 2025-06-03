package model;

import java.util.ArrayList;

public class HousePolicy implements PaymentPolicy {

    private static final double FREE_AMOUNT = 500;

    @Override
    public double calculateTotal(double rawTotal, ArrayList<Order> orders) {
        return Math.max(0, rawTotal - FREE_AMOUNT);
    }

    @Override
    public String getDescription() {
        return "Cliente de la casa: $500 de consumo bonificado por servicio.";
    }
}