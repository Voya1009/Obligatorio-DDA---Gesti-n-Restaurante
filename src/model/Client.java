package model;

import java.util.ArrayList;

public class Client {

    private String number;
    private String password;
    private String name;
    private PaymentPolicy type;
    private Service currentService;

    public Client(String number, String password, String name, PaymentPolicy type) {
        this.number = number;
        this.password = password;
        this.name = name;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public PaymentPolicy getPaymentPolicy() {
        return type;
    }

    public void changeType(PaymentPolicy newType) {
        this.type = newType;
    }

    public double serviceCost(double total, ArrayList<Order> orders) {
        return type.calculateTotal(total, orders);
    }

    public String getBenefitDescription() {
        return type.getDescription();
    }

    public Service getService() {
        return currentService;
    }

    public void newService() {
        this.currentService = new Service();
    }

    public void endService() {
        this.currentService.endService();
        this.currentService = null;
    }
}
