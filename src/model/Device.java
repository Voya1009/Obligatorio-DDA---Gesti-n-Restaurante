package model;

import system.*;

public class Device {

    private final String id;
    private Client client;

    public Device(String id) {
        this.id = id;
        this.client = null;
    }

    public boolean isAvailable() {
        return client == null;
    }

    public Client getClient() {
        return client;
    }

    public void assignClient(Client client) throws SystemException {
        if (!isAvailable()) {
            throw new SystemException("El dispositivo ya está ocupado.");
        }
        this.client = client;
        
    }

    public void releaseClient() {
        this.client = null;
    }

    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return id + (client != null ? " (ocupado)" : " (libre)");
    }
}
