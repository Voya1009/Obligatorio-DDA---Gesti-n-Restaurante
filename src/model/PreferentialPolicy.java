package model;

import java.util.ArrayList;

public class PreferentialPolicy implements PaymentPolicy {

    private static final double WATER_PRICE = 80;
    private static final double DISCOUNT_THRESHOLD = 2000;
    private static final double DISCOUNT_RATE = 0.05;

    @Override
    public double calculateTotal(double rawTotal, ArrayList<Order> orders) {
        long waterCount = orders.stream()
                .filter(o -> o.getItem().getName().equalsIgnoreCase("Agua Mineral"))
                .count();

        double discountWater = waterCount * WATER_PRICE;
        double subtotal = rawTotal - discountWater;

        if (subtotal > DISCOUNT_THRESHOLD) {
            subtotal -= subtotal * DISCOUNT_RATE;
        }
        return Math.max(0, subtotal);
    }

    @Override
    public String getDescription() {
        return "Cliente preferencial: agua mineral sin costo y 5% de descuento si supera los $2000.";
    }
}
