package model;

public class Ingredient {

    private final Supply supply;
    private int quantity;

    public Ingredient(Supply supply, int quantity) {
        this.supply = supply;
        this.quantity = quantity;
    }

    public Supply getSupply() {
        return supply;
    }

    public int getQuantity() {
        return quantity;
    }
}
