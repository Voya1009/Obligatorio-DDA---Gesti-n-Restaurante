package model;

import java.util.ArrayList;

public class Item {

    private String name;
    private double price;
    private Category category;
    private ProcessingUnit assignedPU;
    private ArrayList<Ingredient> ingredients;

    public Item(String name, int price, Category cat, ProcessingUnit assignedPU) {
        this.name = name;
        this.price = price;
        this.category = cat;
        this.assignedPU = assignedPU;
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setCost(int price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        if (!category.getItems().contains(this)) category.addItem(this);
    }

    public ProcessingUnit getPU() {
        return assignedPU;
    }

    public void setAssignedUP(ProcessingUnit assignedPU) {
        this.assignedPU = assignedPU;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Item{" + "name=" + name + ", price=" + price + '}';
    }
}
