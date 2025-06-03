package view;

import controller.ManagerLoginController;

import javax.swing.*;
import java.awt.*;

public class ManagerLoginView extends JFrame {

    private final ManagerLoginController controller;

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton loginButton = new JButton("Iniciar sesión");

    public ManagerLoginView(ManagerLoginController controller) {
        this.controller = controller;

        setTitle("Login Gestor");
        setSize(350, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        buildUI();
        bindEvents();
        setVisible(true);
    }

    private void buildUI() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        inputPanel.add(new JLabel("Usuario:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Contraseña:"));
        inputPanel.add(passwordField);

        add(inputPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void bindEvents() {
        loginButton.addActionListener(e -> controller.handleLogin());
    }

    // Getters para el controller

    public String getUsernameField() {
        return usernameField.getText();
    }

    public String getPasswordField() {
        return new String(passwordField.getPassword());
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atención", JOptionPane.INFORMATION_MESSAGE);
    }

    public void close() {
        dispose();
    }
}

