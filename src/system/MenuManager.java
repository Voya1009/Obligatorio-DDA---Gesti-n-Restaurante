package system;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class MenuManager {

    private List<Item> items = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Supply> supplies = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
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

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<Category> getCategories() {
        return categories;
    }

    // ───── SUPPLIES ─────
    public void addSupply(Supply supply) {
        supplies.add(supply);
    }

    public List<Supply> getSupplies() {
        return supplies;
    }
}
