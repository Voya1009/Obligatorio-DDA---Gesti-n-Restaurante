package model;

import system.*;

public class Supply {

    private String name;
    private int minStock;
    private int stock;

    public Supply(String name, int minStock, int stock) {
        this.name = name;
        this.minStock = minStock;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public int getStock() {
        return stock;
    }

    public void increaseStock(int count) throws SystemException {
        if (count <= 0) {
            throw new SystemException("No se puede aumentar con una cantidad menor a 1.");
        }
        this.stock += count;
    }

    public void decreaseStock(int count) throws SystemException {
        if (count <= 0) {
            throw new SystemException("No se puede disminuir una cantidad menor a 1.");
        }
        if (stock < count || stock <= minStock) {
            throw new SystemException("Stock insuficiente de " + name + ".");
        }
        this.stock -= count;
    }
}
