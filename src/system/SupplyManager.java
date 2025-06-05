package system;

import model.*;
import observer.*;

import java.util.ArrayList;
import java.util.List;

public class SupplyManager {

    private final List<Supply> supplies;
    private final Menu menu;
    private final UserManager userManager;
    private final ObserverManager observerManager;

    public SupplyManager(MenuManager menuManager, UserManager userManager, ObserverManager observerManager) {
        this.supplies = new ArrayList<>();
        this.menu = menuManager.getMenu();
        this.userManager = userManager;
        this.observerManager = observerManager;
    }

    public void addSupply(Supply supply) {
        supplies.add(supply);
    }

    public List<Supply> getSupplies() {
        return supplies;
    }

    public void handleOutOfStock(Supply s) {
        List<Item> toRemove = new ArrayList<>();
        for (Item item : menu.getItems()) {
            for (Ingredient ing : item.getIngredients()) {
                if (ing.getSupply().equals(s)) {
                    toRemove.add(item);
                    break;
                }
            }
        }
        for (Item item : toRemove) {
            menu.removeItem(item);
            for (Client c : userManager.getClients()) {
                Service service = c.getService();
                if (service != null) {
                    service.removeUnconfirmedOrdersByItem(item);
                }
            }
        }
        observerManager.notifyAllObservers();
    }
}
