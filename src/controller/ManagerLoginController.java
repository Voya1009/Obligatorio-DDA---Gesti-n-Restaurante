package controller;

import model.Manager;
import model.Device;
import system.SystemException;
import system.SystemFacade;
import view.ManagerLoginView;

public class ManagerLoginController {

    private final SystemFacade system;
    private final ManagerLoginView view;

    public ManagerLoginController(SystemFacade system) {
        this.system = system;
        this.view = new ManagerLoginView(this);
    }

    public void handleLogin() {
        String username = view.getUsernameField();
        String password = view.getPasswordField();
        try {
            Manager manager = system.getManager(username, password);
            view.showMessage("Inicio de sesión exitoso.");
            view.close();
            new ManagerController(system, manager);
        } catch (SystemException e) {
            view.showMessage(e.getMessage());
        }
    }
}
