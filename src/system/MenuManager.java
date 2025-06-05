package system;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuManager {

    private Menu menu;

    public MenuManager() {
        this.menu = new Menu("Principal");
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Item> getItems() {
        return menu.getItems();
    }

    public void addItem(Item i) {
        menu.addItem(i);
    }

    public void addCategory(Category c) {
        menu.addCategory(c);
    }

    public List<Category> getCategories() {
        return menu.getCategories();
    }

    public List<Item> getItemsByCategory(Category category) {
        return menu.getItems().stream()
                .filter(item -> item.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public void removeItem(Item item) {
        menu.removeItem(item);
    }
}
