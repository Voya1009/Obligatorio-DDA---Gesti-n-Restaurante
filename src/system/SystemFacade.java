package system;

import model.*;
import observer.*;

import java.util.List;

public class SystemFacade {

    private static SystemFacade instance;
    private final UserManager userManager;
    private final DeviceManager deviceManager;
    private final MenuManager menuManager;
    private final ProcessingUnitManager unitManager;
    private final ObserverManager observerManager;

    private SystemFacade() {
        this.userManager = new UserManager();
        this.deviceManager = new DeviceManager();
        this.menuManager = new MenuManager();
        this.unitManager = new ProcessingUnitManager();
        this.observerManager = new ObserverManager();
    }

    public static SystemFacade getInstance() {
        if (instance == null) {
            instance = new SystemFacade();
        }
        return instance;
    }

    public Client getClient(String user, String pass) throws SystemException {
        return userManager.getClient(user, pass);
    }

    public Manager getManager(String user, String pass) throws SystemException {
        return userManager.getManager(user, pass);
    }

    public List<Client> getClients() {
        return userManager.getClients();
    }

    public List<Manager> getManagers() {
        return userManager.getManagers();
    }

    public Device getAvailableDevice() throws SystemException {
        return deviceManager.getAvailableDevice();
    }

    public void releaseDevice(Device device) {
        deviceManager.releaseDevice(device);
        notifyObservers();
    }

    public void addDevice(Device device) {
        deviceManager.addDevice(device);
    }

    public List<Device> getDevices() {
        return deviceManager.getDevices();
    }

    public List<Item> getMenuItems() {
        return menuManager.getItems();
    }

    public List<Category> getCategories() {
        return menuManager.getCategories();
    }

    public void addCategory(Category c) {
        menuManager.addCategory(c);
    }

    public void addItem(Item item) {
        menuManager.addItem(item);
    }

    public void addSupply(Supply s) {
        menuManager.addSupply(s);
    }

    public List<Supply> getSupplies() {
        return menuManager.getSupplies();
    }

    public void addProcessingUnit(ProcessingUnit unit) {
        unitManager.addUnit(unit);
    }

    public List<ProcessingUnit> getProcessingUnits() {
        return unitManager.getAllUnits();
    }

    public void submitOrderToUnit(Order order) throws SystemException {
        unitManager.submitOrderToUnit(order);
        notifyObservers();
    }

    public List<Order> getOrdersByManager(Manager m, OrderState... states) {
        return userManager.getOrdersByManager(m, states);
    }

    public List<Order> getOrdersByUnitAndState(ProcessingUnit unit, OrderState state) {
        return userManager.getOrdersByUnitAndState(unit, state);
    }

    public void addObserver(Observer o) {
        observerManager.addObserver(o);
    }

    public void notifyObservers() {
        observerManager.notifyAllObservers();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public ProcessingUnitManager getProcessingUnitManager() {
        return unitManager;
    }
}
