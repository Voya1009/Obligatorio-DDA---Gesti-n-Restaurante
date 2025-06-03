package view;

import controller.ClientController;
import model.Item;
import model.Order;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientView extends JFrame {

    private final ClientController controller;

    private final DefaultListModel<Item> itemModel = new DefaultListModel<>();
    private final DefaultListModel<Order> orderModel = new DefaultListModel<>();

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton btnLogin = new JButton("Iniciar sesión");

    private final JLabel clientNameLabel = new JLabel("Cliente: no logueado");
    private final JLabel totalLabel = new JLabel("Total: $0");

    private final JList<Item> itemList = new JList<>(itemModel);
    private final JTextField commentField = new JTextField();
    private final JButton btnAddOrder = new JButton("Agregar pedido");

    private final JList<Order> orderList = new JList<>(orderModel);
    private final JButton btnConfirm = new JButton("Confirmar pedidos");
    private final JButton btnCancel = new JButton("Cancelar pedido(s)");
    private final JButton btnFinalize = new JButton("Finalizar servicio");

    public ClientView(ClientController controller) {
        this.controller = controller;

        setTitle("Vista Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 650));
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout(5, 5));

        buildTopPanel();
        buildCenterPanel();
        buildBottomPanel();

        pack();
        setVisible(true);
    }

    private void buildTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel loginPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));

        JPanel fields = new JPanel(new GridLayout(2, 2, 5, 5));
        fields.add(new JLabel("Número:"));
        fields.add(usernameField);
        fields.add(new JLabel("Contraseña:"));
        fields.add(passwordField);
        loginPanel.add(fields);
        loginPanel.add(btnLogin);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información del Servicio"));
        infoPanel.add(clientNameLabel, BorderLayout.WEST);
        infoPanel.add(totalLabel, BorderLayout.EAST);

        topPanel.add(loginPanel);
        topPanel.add(infoPanel);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        btnLogin.addActionListener(e -> controller.handleLogin());
    }

    private void buildCenterPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(300);
        splitPane.setOneTouchExpandable(true);

        JPanel itemPanel = new JPanel(new BorderLayout(5, 5));
        itemPanel.setBorder(BorderFactory.createTitledBorder("Ítems disponibles"));

        itemPanel.add(new JLabel("Seleccione un ítem y agregue comentario:"), BorderLayout.NORTH);

        itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollItem = new JScrollPane(itemList);
        itemPanel.add(scrollItem, BorderLayout.CENTER);

        JPanel commentPanel = new JPanel(new BorderLayout(5, 5));
        commentPanel.add(new JLabel("Comentario:"), BorderLayout.WEST);
        commentPanel.add(commentField, BorderLayout.CENTER);
        commentPanel.add(btnAddOrder, BorderLayout.EAST);
        itemPanel.add(commentPanel, BorderLayout.SOUTH);

        JPanel orderPanel = new JPanel(new BorderLayout(5, 5));
        orderPanel.setBorder(BorderFactory.createTitledBorder("Pedidos"));

        orderPanel.add(new JLabel("Pedidos actuales en servicio:"), BorderLayout.NORTH);

        orderList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollOrder = new JScrollPane(orderList);
        orderPanel.add(scrollOrder, BorderLayout.CENTER);

        splitPane.setLeftComponent(itemPanel);
        splitPane.setRightComponent(orderPanel);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        btnAddOrder.addActionListener(e -> controller.handleAddOrder());
    }

    private void buildBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        bottomPanel.add(btnConfirm);
        bottomPanel.add(btnCancel);
        bottomPanel.add(btnFinalize);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        btnConfirm.addActionListener(e -> controller.handleConfirmOrders());
        btnCancel.addActionListener(e -> controller.handleCancelOrders());
        btnFinalize.addActionListener(e -> controller.handleFinalizeService());
    }

    public String getUsernameField() {
        return usernameField.getText().trim();
    }

    public String getPasswordField() {
        return new String(passwordField.getPassword()).trim();
    }

    public Item getSelectedItem() {
        return itemList.getSelectedValue();
    }

    public List<Item> getSelectedItems() {
        return itemList.getSelectedValuesList();
    }

    public List<Order> getSelectedOrders() {
        return orderList.getSelectedValuesList();
    }

    public String getCommentField() {
        return commentField.getText().trim();
    }

    public void setClientName(String name) {
        clientNameLabel.setText("Cliente: " + name);
    }

    public void updateItemList(List<Item> items) {
        itemModel.clear();
        for (Item i : items) {
            itemModel.addElement(i);
        }
    }

    public void updateOrderList(List<Order> orders) {
        orderModel.clear();
        for (Order o : orders) {
            orderModel.addElement(o);
        }
    }

    public void updateServiceTotal(double total) {
        totalLabel.setText("Total: $" + total);
    }

    public void clearOrderList() {
        orderModel.clear();
    }

    public void clearOrderInputs() {
        commentField.setText("");
        itemList.clearSelection();
    }

    public void clearServiceData() {
        clearOrderList();
        updateServiceTotal(0);
        clientNameLabel.setText("Cliente: no logueado");
        usernameField.setText("");
        passwordField.setText("");
        commentField.setText("");
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atención", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showPaymentDetails(double total, double benefit) {
        String textoPago = String.format(
                "<html><div style='text-align:center;'>"
                + "Beneficio aplicado: $%.2f<br>"
                + "Total a pagar: $%.2f"
                + "</div></html>", benefit, total);

        JOptionPane.showMessageDialog(
                this,
                textoPago,
                "Detalle de pago",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
