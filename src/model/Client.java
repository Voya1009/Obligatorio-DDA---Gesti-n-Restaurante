package model;

import java.util.ArrayList;

public class Client {

    private String number;
    private String password;
    private String name;
    private PaymentPolicy clientPolicy;
    private Service currentService;

    public Client(String number, String password, String name, PaymentPolicy policy) {
        this.number = number;
        this.password = password;
        this.name = name;
        this.clientPolicy = policy;
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
        return clientPolicy;
    }

    public void changePolicy(PaymentPolicy policy) {
        this.clientPolicy = policy;
    }

    public double serviceCost(double total, ArrayList<Order> orders) {
        return clientPolicy.calculateTotal(total, orders);
    }

    public String getBenefitDescription() {
        return clientPolicy.getDescription();
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
