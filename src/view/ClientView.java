package view;

import controller.ClientController;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class ClientView extends JFrame {

    private final ClientController controller;

    private final DefaultListModel<Category> categoryModel = new DefaultListModel<>();
    private final DefaultListModel<Item> itemModel = new DefaultListModel<>();
    private final String[] orderTableColumns = {"Ítem", "Comentario", "Estado", "Unidad", "Gestor", "Precio"};
    private final DefaultTableModel orderTableModel = new DefaultTableModel(orderTableColumns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable orderTable = new JTable(orderTableModel);
    private final List<Order> currentOrders = new ArrayList<>();

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton btnLogin = new JButton("Iniciar sesión");

    private final JLabel totalLabel = new JLabel("Total: $0  ");

    private final JList<Category> categoryList = new JList<>(categoryModel);
    private final JList<Item> itemList = new JList<>(itemModel);
    private final JTextField commentField = new JTextField();
    private final JButton btnAddOrder = new JButton("Agregar pedido");

    private final JButton btnConfirm = new JButton("Confirmar pedido(s)");
    private final JButton btnCancel = new JButton("Cancelar pedido(s)");
    private final JButton btnFinalize = new JButton("Finalizar servicio");

    private final JLabel messageLabel = new JLabel(" ");
    private Timer clearMessageTimer;
    private final LinkedList<String> messageHistory = new LinkedList<>();

    public ClientView(ClientController controller) {
        this.controller = controller;

        setTitle("Servicio RestApp");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));
        pack();
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
        gbc.insets = new Insets(10, 15, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        gbc.gridx = 0;
        loginPanel.add(new JLabel("Número:"), gbc);

        gbc.gridx = 1;
        usernameField.setPreferredSize(new Dimension(130, 25));
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 2;
        loginPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 3;
        passwordField.setPreferredSize(new Dimension(130, 25));
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 4;
        btnLogin.setPreferredSize(new Dimension(130, 30));
        loginPanel.add(btnLogin, gbc);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Costo del servicio"));
        infoPanel.add(totalLabel, BorderLayout.EAST);

        topPanel.add(loginPanel);
        topPanel.add(infoPanel);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        btnLogin.addActionListener(e -> controller.handleLogin());

        usernameField.setFocusTraversalKeysEnabled(true);
        passwordField.setFocusTraversalKeysEnabled(true);
        btnLogin.setFocusTraversalKeysEnabled(true);

        usernameField.setNextFocusableComponent(passwordField);
        passwordField.setNextFocusableComponent(btnLogin);
    }

    private void buildCenterPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(250);

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

        itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollItems = new JScrollPane(itemList);
        scrollItems.setBorder(BorderFactory.createTitledBorder("Ítems"));

        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplit.setTopComponent(scrollCategories);
        verticalSplit.setBottomComponent(scrollItems);
        verticalSplit.setResizeWeight(0.3);

        leftPanel.add(verticalSplit, BorderLayout.CENTER);

        JPanel commentPanel = new JPanel(new BorderLayout(5, 5));

        JPanel commentInputPanel = new JPanel(new BorderLayout(5, 5));
        commentInputPanel.setBorder(BorderFactory.createTitledBorder("Comentario"));

        commentField.setPreferredSize(new Dimension(150, 25));
        commentInputPanel.add(commentField, BorderLayout.CENTER);

        commentPanel.add(commentInputPanel, BorderLayout.CENTER);

        btnAddOrder.setPreferredSize(new Dimension(130, 35));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAddOrder);

        commentPanel.add(buttonPanel, BorderLayout.SOUTH);

        leftPanel.add(commentPanel, BorderLayout.SOUTH);

        JPanel orderPanel = new JPanel(new BorderLayout(5, 5));
        orderPanel.setBorder(BorderFactory.createTitledBorder("Pedidos"));

        orderTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollOrders = new JScrollPane(orderTable);
        scrollOrders.setPreferredSize(new Dimension(250, 250));
        orderPanel.add(scrollOrders, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(orderPanel);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        btnAddOrder.addActionListener(e -> controller.handleAddOrder());
    }

    private void buildBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

        Dimension btnSize = new Dimension(150, 40);
        btnConfirm.setPreferredSize(btnSize);
        btnCancel.setPreferredSize(btnSize);
        btnFinalize.setPreferredSize(btnSize);

        bottomPanel.add(btnConfirm);
        bottomPanel.add(btnCancel);
        bottomPanel.add(btnFinalize);

        btnConfirm.addActionListener(e -> controller.handleConfirmOrders());
        btnCancel.addActionListener(e -> controller.handleCancelOrders());
        btnFinalize.addActionListener(e -> controller.handleFinalizeService());

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Mensajes del sistema"));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(255, 255, 180));
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        messageLabel.setPreferredSize(new Dimension(getWidth(), 50));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BorderLayout());
        bottomContainer.add(bottomPanel, BorderLayout.NORTH);
        bottomContainer.add(messagePanel, BorderLayout.SOUTH);

        getContentPane().add(bottomContainer, BorderLayout.SOUTH);
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
        int[] selectedRows = orderTable.getSelectedRows();
        List<Order> selectedOrders = new ArrayList<>();
        for (int row : selectedRows) {
            if (row >= 0 && row < currentOrders.size()) {
                selectedOrders.add(currentOrders.get(row));
            }
        }
        return selectedOrders;
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
        clearOrderList();
        for (Order o : orders) {
            Object[] row = {
                o.getItem().getName(),
                o.getComment(),
                o.getState().toString(),
                o.getAssignedUP().getName(),
                (o.getManager() != null ? o.getManager().getUsername() : "-"),
                "$" + Math.round(o.getItem().getPrice())
            };
            orderTableModel.addRow(row);
            currentOrders.add(o);
        }
    }

    public void updateServiceTotal(double total) {
        totalLabel.setText("Total: $" + Math.round(total));
    }

    public void clearOrderList() {
        currentOrders.clear();
        orderTableModel.setRowCount(0);
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
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String timedMessage = "[" + time + "] " + msg;
        messageHistory.addFirst(timedMessage);
        while (messageHistory.size() > 3) {
            messageHistory.removeLast();
        }
        StringBuilder html = new StringBuilder("<html>");
        for (int i = messageHistory.size() - 1; i >= 0; i--) {
            html.append(messageHistory.get(i)).append("<br>");
        }
        html.append("</html>");
        messageLabel.setText(html.toString());
        if (clearMessageTimer != null && clearMessageTimer.isRunning()) {
            clearMessageTimer.stop();
        }
        clearMessageTimer = new Timer(5000, e -> {
            messageLabel.setText(" ");
            messageHistory.clear();
        });
        clearMessageTimer.setRepeats(false);
        clearMessageTimer.start();
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
