package view;

import controller.ClientController;
import controller.ManagerLoginController;
import system.*;
import model.Device;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private final SystemFacade system;

    public MainView(SystemFacade system) {
        this.system = system;

        setTitle("Sistema de Gestión del Restaurante");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        buildMenuBar();
        buildWelcomeLabel();

        setVisible(true);
    }

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Nuevo Panel");

        JMenuItem clientItem = new JMenuItem("Cliente");
        clientItem.addActionListener(e -> {
            try {
                Device device = system.getAvailableDevice();
                new ClientController(system, device);
            } catch (SystemException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem managerItem = new JMenuItem("Gestor");
        managerItem.addActionListener(e -> {new ManagerLoginController(system);});
        menu.add(clientItem);
        menu.add(managerItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void buildWelcomeLabel() {
        JLabel welcome = new JLabel("Bienvenido al Sistema del Restaurante", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcome, BorderLayout.CENTER);
    }
}
