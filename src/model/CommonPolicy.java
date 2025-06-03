package model;

import java.util.ArrayList;

public class CommonPolicy implements PaymentPolicy {

    @Override
    public double calculateTotal(double rawTotal, ArrayList<Order> orders) {
        return rawTotal;
    }

    @Override
    public String getDescription() {
        return "Cliente común: sin beneficios.";
    }
}
