package view;

import controller.ClientController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientView extends JFrame {

    private final ClientController controller;

    private final DefaultListModel<Category> categoryModel = new DefaultListModel<>();
    private final DefaultListModel<Item> itemModel = new DefaultListModel<>();
    private final DefaultListModel<Order> orderModel = new DefaultListModel<>();

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton btnLogin = new JButton("Iniciar sesión");

    private final JLabel totalLabel = new JLabel("Total: $0  ");

    private final JList<Category> categoryList = new JList<>(categoryModel);
    private final JList<Item> itemList = new JList<>(itemModel);
    private final JTextField commentField = new JTextField();
    private final JButton btnAddOrder = new JButton("Agregar pedido");

    private final JList<Order> orderList = new JList<>(orderModel);
    private final JButton btnConfirm = new JButton("Confirmar pedido(s)");
    private final JButton btnCancel = new JButton("Cancelar pedido(s)");
    private final JButton btnFinalize = new JButton("Finalizar servicio");

    public ClientView(ClientController controller) {
        this.controller = controller;

        setTitle("Servicio RestApp");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("  Número:"), gbc);

        gbc.gridx = 1;
        usernameField.setPreferredSize(new Dimension(130, 25));
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("  Contraseña:"), gbc);

        gbc.gridx = 1;
        passwordField.setPreferredSize(new Dimension(130, 25));
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnLogin.setPreferredSize(new Dimension(130, 35));
        loginPanel.add(btnLogin, gbc);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Costo del servicio"));
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

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));

        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollCategories = new JScrollPane(categoryList);
        scrollCategories.setBorder(BorderFactory.createTitledBorder("Categorías"));
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Category selected = categoryList.getSelectedValue();
                if (selected != null) {
                    updateItemList(selected.getItems());
                }
            }
        });
        leftPanel.add(scrollCategories, BorderLayout.NORTH);

        itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollItems = new JScrollPane(itemList);
        scrollItems.setBorder(BorderFactory.createTitledBorder("Ítems"));
        leftPanel.add(scrollItems, BorderLayout.CENTER);

        JPanel commentPanel = new JPanel(new BorderLayout(10, 10));
        commentPanel.add(new JLabel("  Comentario:"), BorderLayout.WEST);
        commentPanel.add(commentField, BorderLayout.CENTER);
        commentPanel.add(btnAddOrder, BorderLayout.EAST);
        leftPanel.add(commentPanel, BorderLayout.SOUTH);

        JPanel orderPanel = new JPanel(new BorderLayout(5, 5));
        orderPanel.setBorder(BorderFactory.createTitledBorder("Pedidos"));

        orderList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollOrders = new JScrollPane(orderList);
        orderPanel.add(scrollOrders, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(orderPanel);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        btnAddOrder.addActionListener(e -> controller.handleAddOrder());
    }

    private void buildBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        Dimension btnSize = new Dimension(160, 40);
        btnConfirm.setPreferredSize(btnSize);
        btnCancel.setPreferredSize(btnSize);
        btnFinalize.setPreferredSize(btnSize);

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

    public void setCategories(List<Category> categories) {
        categoryModel.clear();
        for (Category c : categories) {
            categoryModel.addElement(c);
        }
        itemModel.clear();
    }

    public void updateTitle(String clientName) {
        setTitle("Servicio RestApp - Cliente: " + clientName);
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
        totalLabel.setText("Total: $" + Math.round(total));
    }

    public void clearOrderList() {
        orderModel.clear();
    }

    public void clearOrderInputs() {
        commentField.setText("");
        itemList.clearSelection();
    }

    public void clearServiceData() {
        setTitle("Servicio RestApp");
        clearOrderList();
        updateServiceTotal(0);
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
