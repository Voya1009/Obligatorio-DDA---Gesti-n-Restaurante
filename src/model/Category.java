package model;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;
    private ArrayList<Item> items;

    public Category(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        if (!items.contains(item)) {
            items.add(item);
            if (item.getCategory() != this) {
                item.setCategory(this);
            }
        }
    }

    public void removeItem(Item item) {
        if (items.remove(item)) {
            if (item.getCategory() == this) {
                item.setCategory(null);
            }
        }
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return name;
    }
}
