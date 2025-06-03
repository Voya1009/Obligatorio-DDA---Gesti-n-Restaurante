package view;

import controller.ManagerController;
import model.Order;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManagerView extends JFrame {

    private final ManagerController controller;

    private final DefaultListModel<Order> pendingModel = new DefaultListModel<>();
    private final DefaultListModel<Order> takenModel = new DefaultListModel<>();

    private final JList<Order> pendingList = new JList<>(pendingModel);
    private final JList<Order> takenList = new JList<>(takenModel);

    private final JButton btnTake = new JButton("Tomar pedido");
    private final JButton btnFinalize = new JButton("Finalizar pedido");
    private final JButton btnDeliver = new JButton("Entregar pedido");

    public ManagerView(ManagerController controller) {
        this.controller = controller;
        setTitle("Vista Gestor");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        buildUI();
        bindEvents();
        setVisible(true);
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Pedidos disponibles (por confirmar):"), BorderLayout.NORTH);
        pendingList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        leftPanel.add(new JScrollPane(pendingList), BorderLayout.CENTER);
        leftPanel.add(btnTake, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Pedidos tomados:"), BorderLayout.NORTH);
        takenList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        rightPanel.add(new JScrollPane(takenList), BorderLayout.CENTER);

        JPanel rightButtons = new JPanel(new GridLayout(1, 2));
        rightButtons.add(btnFinalize);
        rightButtons.add(btnDeliver);
        rightPanel.add(rightButtons, BorderLayout.SOUTH);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void bindEvents() {
        btnTake.addActionListener(e -> {
            List<Order> selected = pendingList.getSelectedValuesList();
            controller.handleAdvanceOrders(selected);
        });

        btnFinalize.addActionListener(e -> {
            List<Order> selected = takenList.getSelectedValuesList();
            controller.handleAdvanceOrders(selected);
        });

        btnDeliver.addActionListener(e -> {
            List<Order> selected = takenList.getSelectedValuesList();
            controller.handleAdvanceOrders(selected);
        });
    }

    public void updatePendingList(List<Order> list) {
        pendingModel.clear();
        for (Order o : list) {
            pendingModel.addElement(o);
        }
    }

    public void updateTakenList(List<Order> list) {
        takenModel.clear();
        for (Order o : list) {
            takenModel.addElement(o);
        }
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atención", JOptionPane.INFORMATION_MESSAGE);
    }
}
