package model;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private String name;
    private List<Item> items;
    private List<Category> categories;

    public Menu(String name) {
        this.name = name;
        this.categories = new ArrayList();
        this.items = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addItem(Item item) {
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    public void addCategory(Category category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public List<Item> getItemsByCategory(Category category) {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (item.getCategory().equals(category)) {
                result.add(item);
            }
        }
        return result;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    @Override
    public String toString() {
        return name;
    }
}
