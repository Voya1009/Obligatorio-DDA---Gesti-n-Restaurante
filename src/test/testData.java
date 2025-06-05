package test;

import model.*;
import system.*;

public class testData {

    public static void load(SystemFacade system) {
        // --- Supplies ---
        Supply queso = new Supply("Queso", 5, 20);
        Supply tomate = new Supply("Tomate", 5, 15);
        Supply carne = new Supply("Carne", 10, 30);
        Supply lechuga = new Supply("Lechuga", 5, 25);
        Supply cafe = new Supply("Cafe", 10, 50);
        Supply agua = new Supply("AguaMineral", 10, 50);
        Supply ginebra = new Supply("Ginebra", 5, 20);
        system.addSupply(queso);
        system.addSupply(tomate);
        system.addSupply(carne);
        system.addSupply(lechuga);
        system.addSupply(cafe);
        system.addSupply(agua);
        system.addSupply(ginebra);

        // --- ProcessingUnits ---
        ProcessingUnit cocina = new ProcessingUnit("Cocina");
        ProcessingUnit bar = new ProcessingUnit("Bar");
        system.addProcessingUnit(cocina);
        system.addProcessingUnit(bar);

        // --- Categories ---
        Category entradas = new Category("Entradas");
        Category principales = new Category("Platos Principales");
        Category bebidas = new Category("Bebidas");
        Category postres = new Category("Postres");
        system.addCategoryToMenu(entradas);
        system.addCategoryToMenu(principales);
        system.addCategoryToMenu(bebidas);
        system.addCategoryToMenu(postres);

        // --- Items ---
        Item empanada = new Item("Empanada", 120, entradas, cocina);
        empanada.addIngredient(new Ingredient(queso, 1));
        empanada.addIngredient(new Ingredient(carne, 1));
        entradas.addItem(empanada);
        system.addItemToMenu(empanada);

        Item milanesa = new Item("Milanesa con Papas", 450, principales, cocina);
        milanesa.addIngredient(new Ingredient(carne, 2));
        milanesa.addIngredient(new Ingredient(lechuga, 1));
        principales.addItem(milanesa);
        system.addItemToMenu(milanesa);

        Item pizza = new Item("Pizza", 550, principales, cocina);
        pizza.addIngredient(new Ingredient(queso, 2));
        pizza.addIngredient(new Ingredient(tomate, 1));
        pizza.addIngredient(new Ingredient(lechuga, 1));
        principales.addItem(pizza);
        system.addItemToMenu(pizza);

        Item ginTonic = new Item("Gin Tonic", 300, bebidas, bar);
        ginTonic.addIngredient(new Ingredient(ginebra, 1));
        ginTonic.addIngredient(new Ingredient(agua, 1));
        bebidas.addItem(ginTonic);
        system.addItemToMenu(ginTonic);

        Item cafeItem = new Item("Café", 100, bebidas, bar);
        cafeItem.addIngredient(new Ingredient(cafe, 1));
        bebidas.addItem(cafeItem);
        system.addItemToMenu(cafeItem);

        Item helado = new Item("Helado", 150, postres, cocina);
        helado.addIngredient(new Ingredient(queso, 1));
        postres.addItem(helado);
        system.addItemToMenu(helado);

        // --- Devices (tablets) ---
        for (int i = 1; i <= 5; i++) {
            system.addDevice(new Device("Tablet-" + i));
        }

        // --- Clients (4 tipos) ---
        Client comun = new Client("1001", "pass1", "Ana Pérez", new CommonPolicy());
        Client frecuente = new Client("1002", "pass2", "Luis García", new FrequentPolicy());
        Client preferencial = new Client("1003", "pass3", "María López", new PreferentialPolicy());
        Client deLaCasa = new Client("1004", "pass4", "Jorge Díaz", new HousePolicy());
        system.getUserManager().addClient(comun);
        system.getUserManager().addClient(frecuente);
        system.getUserManager().addClient(preferencial);
        system.getUserManager().addClient(deLaCasa);

        // --- Managers (uno por unidad) ---
        Manager jefeCocina = new Manager("cocina1", "1234", "Pedro Chef", cocina);
        Manager jefeBar = new Manager("bar1", "1234", "Laura Barista", bar);
        system.getUserManager().addManager(jefeCocina);
        system.getUserManager().addManager(jefeBar);
    }
}
