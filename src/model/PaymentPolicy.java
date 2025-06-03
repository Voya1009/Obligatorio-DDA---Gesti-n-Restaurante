package model;

import java.util.ArrayList;

public abstract interface PaymentPolicy {

    double calculateTotal(double total, ArrayList<Order> order);

    public String getDescription();
}
