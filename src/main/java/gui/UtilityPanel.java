package gui;

import dao.RoomDAO;
import dao.UtilityDAO;
import entity.Utility;
import utils.Alert;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class UtilityPanel extends JPanel {
    public UtilityPanel() {
        DefaultTableModel model = new DefaultTableModel();
        setPreferredSize(new Dimension(980, 684));
        setBackground(new Color(238, 240, 168));
        setLayout(new BorderLayout(0, 0));

        JPanel infoModel = new JPanel();
        infoModel.setPreferredSize(new Dimension(830, 350));
        infoModel.setBackground(new Color(238, 240, 168));
        infoModel.setBorder(new LineBorder(new Color(139, 141, 94), 2, true)); // set line border
        infoModel.setLayout(new GridBagLayout());
        JLabel lbId = new JLabel("Mã cơ sở vật chất:", JLabel.CENTER);
        txtId = new JTextField();
        txtId.setEnabled(false);
        txtId.setFont(new Font("Tahoma", Font.BOLD, 14));

        JLabel lbType = new JLabel("Loại thiết bị:", JLabel.CENTER);
        txtType = new JTextField();
        txtType.setFont(new Font("Tahoma", Font.PLAIN, 14));


        JLabel lbName = new JLabel("Tên thiết bị:", JLabel.CENTER);
        txtName = new JTextField();
        txtName.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbPricing = new JLabel("Giá:", JLabel.CENTER);
        txtPricing = new JTextField();
        txtPricing.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbDescription = new JLabel("Mô tả:", JLabel.CENTER);
        txtDescription = new JTextArea();
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setRows(3);
        txtDescription.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbRoomId = new JLabel("Mã phòng:", JLabel.CENTER);
        txtRoomId = new JTextField();
        txtRoomId.setFont(new Font("Tahoma", Font.PLAIN, 14));

        List<JComponent> components = Arrays.asList(
                lbId, txtId,
                lbType, txtType,
                lbName, txtName,
                lbPricing, txtPricing,
                lbDescription, txtDescription,
                lbRoomId, txtRoomId
        );

        for (int i = 0; i < components.size(); i++) {
            JComponent component = components.get(i);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = i % 2 == 0 ? 0 : 2;
            c.gridy = i / 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = i % 2 == 0 ? 1.0 : 5.0;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(5, 5, i == components.size() - 1 ? 30 : 5, 5);
            infoModel.add(component, c);
        }

        setOffModel();
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
            add = true;
            setOnModel();
            resetTextModel();
            txtId.setText("");
            txtType.requestFocus();
            btnSave.setEnabled(true);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        });
        btnEdit.addActionListener(e -> {
            add = false;
            setOnModel();
            txtType.requestFocus();
            btnSave.setEnabled(true);
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
        });
        btnSave.addActionListener(e -> {
            String type = txtType.getText();
            String name = txtName.getText();
            String pricing = txtPricing.getText();
            String description = txtDescription.getText();
            String roomId = txtRoomId.getText();
            if (type.isEmpty() || name.isEmpty() || pricing.isEmpty() || description.isEmpty() || roomId.isEmpty()) {
                Alert.showWarning("Vui lòng nhập đầy đủ thông tin");
                return;
            }
            try {
                Double.parseDouble(pricing);
                Integer.parseInt(roomId);
            } catch (NumberFormatException ex) {
                Alert.showWarning("Giá và mã phòng phải là số nguyên");
                return;
            }
            int rid = Integer.parseInt(txtRoomId.getText());
            if (RoomDAO.getInstance().findById(rid) == null) {
                Alert.showWarning("Mã phòng không tồn tại");
                return;
            }
            BigDecimal price = new BigDecimal(pricing);
            Utility utility = Utility
                    .builder()
                    .utilityName(name)
                    .type(type)
                    .pricing(price)
                    .description(description)
                    .roomId(rid)
                    .build();
            if (add) {
                boolean isSuccess = UtilityDAO.getInstance().save(utility);
                if (isSuccess) {
                    Alert.showSuccess("Thêm cơ sở vật chất thành công");
                    btnReset.doClick();
                } else {
                    Alert.showError("Thêm cơ sở vật chất thất bại");
                }
            } else {
                utility.setUtilityId(Integer.parseInt(txtId.getText()));
                boolean isSuccess = UtilityDAO.getInstance().save(utility);
                if (isSuccess) {
                    Alert.showSuccess("Sửa cơ sở vật chất thành công");
                    btnReset.doClick();
                } else {
                    Alert.showError("Sửa cơ sở vật chất thất bại");
                }
            }
        });
        btnDelete.addActionListener(e -> {
            int option = Alert.showConfirm("Bạn có chắc chắn muốn xóa cơ sở vật chất này không?");
            if (option == JOptionPane.YES_OPTION) {
                // Delete the item
                int id = Integer.parseInt(txtId.getText());
                boolean isSuccess = UtilityDAO.getInstance().delete(id);
                if (isSuccess) {
                    Alert.showSuccess("Xóa cơ sở vật chất thành công");
                    btnReset.doClick();
                } else {
                    Alert.showError("Xóa cơ sở vật chất thất bại");
                }
            }
        });
        btnReset.addActionListener(e -> {
            // Code to be executed when the button is clicked
            fillDataTable(model);
            txtId.setText("");
            resetTextModel();
            setOffModel();
            txtName.setFocusable(true);
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
            add = false;
        });

        JPanel showData = new JPanel();
        showData.setPreferredSize(new Dimension(980, 334));
        showData.setBackground(new Color(238, 240, 168));
        showData.setBorder(new LineBorder(new Color(139, 141, 94), 2, true));
        showData.setLayout(new BorderLayout(0, 0));

        model.addColumn("Mã cơ sở vật chất");
        model.addColumn("Loại");
        model.addColumn("Tên cơ sở vật chất");
        model.addColumn("Giá");
        model.addColumn("Mô tả");
        model.addColumn("Mã phòng");
        model.addColumn("Tên phòng");
        model.addColumn("Khu vực");
        fillDataTable(model);

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
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
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
                txtId.setText(model.getValueAt(pos, 0).toString());
                txtType.setText(model.getValueAt(pos, 1).toString());
                txtName.setText(model.getValueAt(pos, 2).toString());
                txtPricing.setText(model.getValueAt(pos, 3).toString());
                txtDescription.setText(model.getValueAt(pos, 4).toString());
                txtRoomId.setText(model.getValueAt(pos, 5).toString());

                setOffModel();
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
                add = false;
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        showData.add(scrollPane, BorderLayout.CENTER);

        add(infoModel, BorderLayout.WEST);
        add(grboxButton, BorderLayout.EAST);
        add(showData, BorderLayout.SOUTH);
    }

    private void setOnModel() {
        toggleModel(true);
    }

    private void setOffModel() {
        toggleModel(false);
    }

    private void toggleModel(boolean state) {
        txtId.setEnabled(false);
        txtType.setEnabled(state);
        txtName.setEnabled(state);
        txtPricing.setEnabled(state);
        txtDescription.setEnabled(state);
        txtRoomId.setEnabled(state);
    }

    private void resetTextModel() {
        txtId.setText("");
        txtType.setText("");
        txtName.setText("");
        txtPricing.setText("");
        txtDescription.setText("");
        txtRoomId.setText("");
    }

    private void fillDataTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Utility b : UtilityDAO.getInstance().findAll()) {
            Object[] data = new Object[15];
            data[0] = b.getUtilityId();
            data[1] = b.getType();
            data[2] = b.getUtilityName();
            data[3] = b.getPricing();
            data[4] = b.getDescription();
            data[5] = b.getRoomId();
            data[6] = b.getRoom().getRoomName();
            data[7] = b.getRoom().getBlock();
            model.addRow(data);
        }
    }

    private boolean add = false;
    private final JTextField txtId;
    private final JTextField txtType;
    private final JTextField txtName;
    private final JTextField txtPricing;
    private final JTextArea txtDescription;
    private final JTextField txtRoomId;
    private final JButton btnAdd;
    private final JButton btnEdit;
    private final JButton btnSave;
    private final JButton btnDelete;
    private final JButton btnReset;
}
