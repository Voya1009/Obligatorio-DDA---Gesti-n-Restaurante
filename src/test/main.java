package test;

import model.*;
import system.*;
import view.*;

public class main {

    public static void main(String[] args) {
        SystemFacade system = SystemFacade.getInstance();

        testData.load(system);

        System.out.println("=== Dispositivos cargados ===");
        for (Device d : system.getDevices()) {
            System.out.println("Dispositivo #" + d.getID() + " - Disponible: " + d.isAvailable());
        }

        System.out.println("\n=== Clientes cargados ===");
        for (Client c : system.getClients()) {
            System.out.println(c.getName() + " - Tipo: " + c.getBenefitDescription());
        }

        System.out.println("\n=== Ítems disponibles ===");
        for (Item i : system.getMenuManager().getItems()) {
            System.out.println(i.getName() + " - $" + i.getPrice() + " - Categoría: " + i.getCategory().getName());
        }

        System.out.println("\n=== Insumos cargados ===");
        for (Supply supply : system.getSupplies()) {
            System.out.println(supply.getName() + " - Stock: " + supply.getStock() + "/" + supply.getMinStock());
        }

        System.out.println("\n=== Gestores ===");
        for (Manager m : system.getManagers()) {
            System.out.println(m.getName() + " - Unidad: " + m.getPU().getName());
        }

        System.out.println("\nSistema inicializado correctamente.");
        new MainView(system);  
    }
}
