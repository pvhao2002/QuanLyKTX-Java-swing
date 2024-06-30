package gui;

import dao.RoomDAO;
import entity.Room;
import utils.Alert;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

public class RoomPanel extends JPanel {
    private boolean addRoom = false;
    private final List<String> listBlock = Arrays.asList("A", "B", "C");
    private final JTextField txtIdRoom;
    private final JTextField txtRoomName;
    private final JTextField txtCapicity;
    private final JTextField txtBlock;
    private final JButton btnAdd;
    private final JButton btnEdit;
    private final JButton btnSave;
    private final JButton btnDelete;
    private final JButton btnReset;


    public RoomPanel() {
        DefaultTableModel model = new DefaultTableModel();
        setPreferredSize(new Dimension(980, 684));
        setBackground(new Color(238, 240, 168));
        setLayout(new BorderLayout(0, 0));

        JPanel infoModel = new JPanel();
        infoModel.setPreferredSize(new Dimension(830, 350));
        infoModel.setBackground(new Color(238, 240, 168));
        infoModel.setBorder(new LineBorder(new Color(139, 141, 94), 2, true)); // set line border
        infoModel.setLayout(new GridBagLayout());
        JLabel lbIdRoom = new JLabel("Mã phòng:", JLabel.CENTER);
        txtIdRoom = new JTextField();
        txtIdRoom.setEnabled(false);
        txtIdRoom.setFont(new Font("Tahoma", Font.BOLD, 14));
        txtIdRoom.setForeground(Color.blue);

        JLabel lbRoomName = new JLabel("Tên phòng:", JLabel.CENTER);
        txtRoomName = new JTextField();
        txtRoomName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtRoomName.setForeground(Color.blue);

        JLabel lbCapicity = new JLabel("Số lượng:", JLabel.CENTER);
        txtCapicity = new JTextField();
        txtCapicity.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtCapicity.setForeground(Color.blue);

        JLabel lbBlock = new JLabel("Khu:", JLabel.CENTER);
        txtBlock = new JTextField();
        txtBlock.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtBlock.setForeground(Color.blue);

        List<JComponent> fieldList = Arrays.asList(
                lbIdRoom,
                txtIdRoom,
                lbRoomName,
                txtRoomName,
                lbCapicity,
                txtCapicity,
                lbBlock,
                txtBlock
        );
        for (int i = 0; i < fieldList.size(); i++) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = i % 2 == 0 ? 0 : 2;
            c.gridy = i / 2;
            c.gridwidth = i % 2 == 0 ? 1 : 5;
            c.gridheight = 1;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = i % 2 == 0 ? 1.0 : 5.0;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(5, 5, 5, 5);
            infoModel.add(fieldList.get(i), c);
        }
        setOffModelRoom(infoModel);
        JPanel grboxButton = new JPanel();
        grboxButton.setPreferredSize(new Dimension(150, 350));
        grboxButton.setBackground(new Color(238, 240, 168));
        AdminMainFrame.initCRUDComponents(grboxButton);
        btnAdd = (JButton) grboxButton.getComponent(0);
        btnEdit = (JButton) grboxButton.getComponent(1);
        btnEdit.setEnabled(false);
        btnSave = (JButton) grboxButton.getComponent(2);
        btnSave.setEnabled(false);
        btnDelete = (JButton) grboxButton.getComponent(3);
        btnDelete.setEnabled(false);
        btnReset = (JButton) grboxButton.getComponent(4);
        btnAdd.addActionListener(e -> {
            addRoom = true;
            setOnModelRoom(infoModel);
            resetTextModelRoom(infoModel);
            txtIdRoom.setText("");
            txtRoomName.requestFocus();
            btnSave.setEnabled(true);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        });
        btnEdit.addActionListener(e -> {
            addRoom = false;
            setOnModelRoom(infoModel);
            txtRoomName.requestFocus();
            btnSave.setEnabled(true);
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
        });
        btnSave.addActionListener(e -> save());
        btnDelete.addActionListener(e -> delete());
        btnReset.addActionListener(e -> {
            // Code to be executed when the button is clicked
            fillDataTableRoom(model);
            txtIdRoom.setText("");
            resetTextModelRoom(infoModel);
            setOffModelRoom(infoModel);
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
            addRoom = false;
        });

        JPanel showData = new JPanel();
        showData.setPreferredSize(new Dimension(980, 334));
        showData.setBackground(new Color(238, 240, 168));
        showData.setBorder(new LineBorder(new Color(139, 141, 94), 2, true));
        showData.setLayout(new BorderLayout(0, 0));

        model.addColumn("Mã phòng");
        model.addColumn("Tên phòng");
        model.addColumn("Số lượng chứa");
        model.addColumn("Khu");
        fillDataTableRoom(model);

        // Create a table using the model
        JTable table = new JTable(model);
        JTableHeader header = table.getTableHeader();

        // set table to read only
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JTextField()) {
                @Override
                public boolean isCellEditable(EventObject anEvent) {
                    return false;
                }
            });
        }
        // Create a custom renderer for the cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Set the custom renderer as the default renderer for all cells
        table.setDefaultRenderer(Object.class, centerRenderer);
        // Adjust the width of each column based on the width of the widest cell in that
        // column
        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            TableColumn column = table.getColumnModel().getColumn(columnIndex);
            int maxWidth = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, columnIndex);
                Object value = table.getValueAt(row, columnIndex);
                Component comp = renderer.getTableCellRendererComponent(table, value, false, false, row, columnIndex);
                maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
            }
            column.setPreferredWidth(maxWidth);
        }

        // Set header of table: color is Black, Font Bold, text align center
        header.setDefaultRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );
                label.setBackground(new Color(221, 221, 227));
                label.setForeground(Color.BLACK);
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int pos = table.rowAtPoint(e.getPoint());
                fillDataToTextBox(table, pos);
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
                addRoom = false;
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        showData.add(scrollPane, BorderLayout.CENTER);

        add(infoModel, BorderLayout.WEST);
        add(grboxButton, BorderLayout.EAST);
        add(showData, BorderLayout.SOUTH);
    }

    private void delete() {
        int option = Alert.showConfirm("Bạn có muốn xóa phòng này không?");
        if (option == JOptionPane.YES_OPTION) {
            // Delete the item
            int id = Integer.parseInt(txtIdRoom.getText());
            boolean isSuccess = RoomDAO.getInstance().delete(id);
            if (isSuccess) {
                Alert.showSuccess("Xóa phòng thành công!");
                btnReset.doClick();
            } else {
                Alert.showError("Xóa phòng thất bại!");
            }
        }
    }

    private void save() {
        String roomName = txtRoomName.getText();
        String capicityText = txtCapicity.getText();
        String blockText = txtBlock.getText();
        boolean isReset = false;
        if (!capicityText.matches("\\d+")) {
            Alert.showError("Số lượng không hợp lệ!");
        } else if (listBlock.stream().noneMatch(b -> b.equalsIgnoreCase(blockText))) {
            Alert.showError("Khu không hợp lệ! Khu phải là A, B hoặc C!");
        } else {
            Room room = Room
                    .builder()
                    .roomName(roomName)
                    .capacity(Integer.parseInt(capicityText))
                    .block(blockText)
                    .build();
            if (addRoom) {
                boolean isSuccess = RoomDAO.getInstance().save(room);
                if (isSuccess) {
                    Alert.showSuccess("Thêm phòng thành công!");
                    isReset = true;
                } else {
                    Alert.showError("Thêm phòng thất bại!");
                }
            } else {
                int id = Integer.parseInt(txtIdRoom.getText());
                room.setRoomId(id);
                boolean isSuccess = RoomDAO.getInstance().save(room);
                if (isSuccess) {
                    Alert.showSuccess("Cập nhật phòng thành công!");
                    isReset = true;
                } else {
                    Alert.showError("Cập nhật phòng thất bại!");
                }
            }
            if (isReset) {
                btnReset.doClick();
            }
        }
    }

    private void fillDataToTextBox(JTable table, int pos) {
        int idbook = Integer.parseInt(table.getValueAt(pos, 0).toString());
        txtIdRoom.setText(idbook + "");
        txtRoomName.setText(table.getValueAt(pos, 1).toString());
        txtCapicity.setText(table.getValueAt(pos, 2).toString());
        txtBlock.setText(table.getValueAt(pos, 3).toString());
    }

    private void setOffModelRoom(JPanel infoModel) {
        JTextField txtTitle = (JTextField) infoModel.getComponent(3);
        JTextField txtAuthor = (JTextField) infoModel.getComponent(5);
        JTextField txtCategory = (JTextField) infoModel.getComponent(7);
        txtTitle.setEnabled(false);
        txtAuthor.setEnabled(false);
        txtCategory.setEnabled(false);
    }

    private void fillDataTableRoom(DefaultTableModel model) {
        model.setRowCount(0);
        for (Room b : RoomDAO.getInstance().findAll()) {
            Object[] data = new Object[15];
            data[0] = b.getRoomId();
            data[1] = b.getRoomName();
            data[2] = b.getCapacity();
            data[3] = b.getBlock();
            model.addRow(data);
        }
    }

    private void resetTextModelRoom(JPanel infoModel) {
        JTextField txtTitle = (JTextField) infoModel.getComponent(3);
        JTextField txtAuthor = (JTextField) infoModel.getComponent(5);
        JTextField txtCategory = (JTextField) infoModel.getComponent(7);
        txtTitle.setText("");
        txtAuthor.setText("");
        txtCategory.setText("");
    }

    private void setOnModelRoom(JPanel infoModel) {
        JTextField txtTitle = (JTextField) infoModel.getComponent(3);
        JTextField txtAuthor = (JTextField) infoModel.getComponent(5);
        JTextField txtCategory = (JTextField) infoModel.getComponent(7);
        txtTitle.setEnabled(true);
        txtAuthor.setEnabled(true);
        txtCategory.setEnabled(true);
    }
}
