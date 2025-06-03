package system;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class UserManager {

    private final List<Client> clients;
    private final List<Manager> managers;

    public UserManager() {
        this.clients = new ArrayList<>();
        this.managers = new ArrayList<>();
    }

    public void addClient(Client c) {
        clients.add(c);
    }

    public void addManager(Manager m) {
        managers.add(m);
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public Client getClient(String number, String password) throws SystemException {
        for (Client c : clients) {
            if (c.getNumber().equals(number) && c.getPassword().equals(password)) {
                return c;
            }
        }
        throw new SystemException("Credenciales incorrectas.");
    }

    public Manager getManager(String username, String password) throws SystemException {
        for (Manager m : managers) {
            if (m.getUsername().equals(username) && m.getPassword().equals(password)) {
                return m;
            }
        }
        throw new SystemException("Credenciales incorrectas.");
    }

    public List<Order> getOrdersByManager(Manager manager, OrderState... states) {
        List<Order> result = new ArrayList<>();
        List<OrderState> validStates = List.of(states);

        for (Client client : clients) {
            Service service = client.getService();
            if (service == null) {
                continue;
            }

            for (Order order : service.getOrders()) {
                if (manager.equals(order.getManager()) && validStates.contains(order.getState())) {
                    result.add(order);
                }
            }
        }

        return result;
    }

    public List<Order> getOrdersByUnitAndState(ProcessingUnit unit, OrderState state) {
        List<Order> result = new ArrayList<>();

        for (Client client : clients) {
            Service service = client.getService();
            if (service == null) {
                continue;
            }

            for (Order order : service.getOrders()) {
                if (unit.equals(order.getItem().getPU()) && order.getState() == state) {
                    result.add(order);
                }
            }
        }

        return result;
    }
}
