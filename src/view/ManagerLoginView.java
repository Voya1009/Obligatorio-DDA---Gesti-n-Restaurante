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
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        buildUI();
        bindEvents();
        setVisible(true);
    }

    private void buildUI() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        fieldsPanel.setMaximumSize(new Dimension(300, 60));

        fieldsPanel.add(new JLabel("Usuario:"));
        fieldsPanel.add(usernameField);
        fieldsPanel.add(new JLabel("Contraseña:"));
        fieldsPanel.add(passwordField);

        usernameField.setPreferredSize(new Dimension(140, 30));
        passwordField.setPreferredSize(new Dimension(140, 30));

        JPanel buttonPanel = new JPanel();
        loginButton.setPreferredSize(new Dimension(140, 35));
        buttonPanel.add(loginButton);

        mainPanel.add(fieldsPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(buttonPanel);

        centerPanel.add(mainPanel, gbc);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void bindEvents() {
        loginButton.addActionListener(e -> controller.handleLogin());
    }

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
