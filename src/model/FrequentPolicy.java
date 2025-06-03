package model;

import java.util.ArrayList;

public class FrequentPolicy implements PaymentPolicy {

    private static final double COFFEE_PRICE = 100; 

    @Override
    public double calculateTotal(double rawTotal, ArrayList<Order> orders) {
        long coffeeCount = orders.stream()
                .filter(o -> o.getItem().getName().equalsIgnoreCase("Café"))
                .count();

        double discount = coffeeCount * COFFEE_PRICE;
        return Math.max(0, rawTotal - discount);
    }

    @Override
    public String getDescription() {
        return "Cliente frecuente: café de invitación.";
    }
}
