package view;

import controller.ManagerController;
import model.Order;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        setTitle("Gestor - " + controller.getManagerName());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (controller.hasUndeliveredOrders()) {
                    showMessage("Aún quedan pedidos sin entregar.");
                } else {
                    dispose();
                }
            }
        });

        setLayout(new BorderLayout());
        buildUI();
        bindEvents();
        setVisible(true);
    }

    private void buildUI() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Pedidos confirmados pendientes:"), BorderLayout.NORTH);
        pendingList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        leftPanel.add(new JScrollPane(pendingList), BorderLayout.CENTER);

        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnTake.setPreferredSize(new Dimension(140, 30));
        leftButtonPanel.add(btnTake);
        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Pedidos tomados:"), BorderLayout.NORTH);
        takenList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        rightPanel.add(new JScrollPane(takenList), BorderLayout.CENTER);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnFinalize.setPreferredSize(new Dimension(140, 30));
        btnDeliver.setPreferredSize(new Dimension(140, 30));
        rightButtonPanel.add(btnFinalize);
        rightButtonPanel.add(btnDeliver);
        rightPanel.add(rightButtonPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(350);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);
    }

    private void bindEvents() {
        btnTake.addActionListener(e -> {
            List<Order> selected = pendingList.getSelectedValuesList();
            controller.handleTakeOrder(selected);
        });

        btnFinalize.addActionListener(e -> {
            List<Order> selected = takenList.getSelectedValuesList();
            controller.handleReadyOrder(selected);
        });

        btnDeliver.addActionListener(e -> {
            List<Order> selected = takenList.getSelectedValuesList();
            controller.handleDeliverOrder(selected);
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
