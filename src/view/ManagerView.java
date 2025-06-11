package view;

import controller.ManagerController;
import model.Order;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class ManagerView extends JFrame {

    private final ManagerController controller;

    private final DefaultListModel<Order> pendingModel = new DefaultListModel<>();
    private final JList<Order> pendingList = new JList<>(pendingModel);

    private final String[] takenTableColumns = {"", "Item", "Comentario", "Cliente", "Fecha y Hora", "Estado"};
    private final DefaultTableModel takenTableModel = new DefaultTableModel(takenTableColumns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable takenTable = new JTable(takenTableModel);

    private final JButton btnTake = new JButton("Tomar pedido");
    private final JButton btnFinalize = new JButton("Finalizar pedido");
    private final JButton btnDeliver = new JButton("Entregar pedido");

    private final JLabel messageLabel = new JLabel(" ");
    private Timer clearMessageTimer;
    private final LinkedList<String> messageHistory = new LinkedList<>();

    public ManagerView(ManagerController controller) {
        this.controller = controller;
        setTitle("Gestor - " + controller.getManagerName());
        setSize(1000, 500);
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
        takenTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        takenTable.getColumnModel().getColumn(0).setMinWidth(0);
        takenTable.getColumnModel().getColumn(0).setMaxWidth(0);
        takenTable.getColumnModel().getColumn(0).setWidth(0);
        JScrollPane tableScroll = new JScrollPane(takenTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnFinalize.setPreferredSize(new Dimension(140, 30));
        btnDeliver.setPreferredSize(new Dimension(140, 30));
        rightButtonPanel.add(btnFinalize);
        rightButtonPanel.add(btnDeliver);
        rightPanel.add(rightButtonPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerLocation(250);

        add(splitPane, BorderLayout.CENTER);

        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(255, 255, 180));
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        messageLabel.setPreferredSize(new Dimension(getWidth(), 50));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Mensajes del sistema"));
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);
    }

    private void bindEvents() {
        btnTake.addActionListener(e -> {
            List<Order> selected = pendingList.getSelectedValuesList();
            controller.handleTakeOrder(selected);
        });

        btnFinalize.addActionListener(e -> {
            int[] selectedRows = takenTable.getSelectedRows();
            List<Order> selected = new ArrayList<>();
            for (int row : selectedRows) {
                selected.add((Order) takenTableModel.getValueAt(row, 0));
            }
            controller.handleReadyOrder(selected);
        });

        btnDeliver.addActionListener(e -> {
            int[] selectedRows = takenTable.getSelectedRows();
            List<Order> selected = new ArrayList<>();
            for (int row : selectedRows) {
                selected.add((Order) takenTableModel.getValueAt(row, 0));
            }
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
        takenTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Order o : list) {
            takenTableModel.addRow(new Object[]{
                o, o.getItem().getName(), o.getComment(), o.getClient().getName(), o.getDate().format(formatter), o.getState()
            });
        }
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
}
