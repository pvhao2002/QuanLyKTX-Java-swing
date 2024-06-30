package gui;

import dao.InvoiceDAO;
import dao.StudentDAO;
import dao.UtilityDAO;
import entity.Invoice;
import entity.Utility;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CreateBillPanel extends JPanel {
    public CreateBillPanel() {
        DefaultTableModel model = new DefaultTableModel();
        setPreferredSize(new Dimension(980, 684));
        setBackground(new Color(238, 240, 168));
        setLayout(new BorderLayout(0, 0));
        loadDataToCombobox();
        listUtilitySelected = new ArrayList<>();
        JPanel infoModel = new JPanel();
        infoModel.setPreferredSize(new Dimension(980, 350));
        infoModel.setBackground(new Color(238, 240, 168));
        infoModel.setBorder(new LineBorder(new Color(139, 141, 94), 2, true)); // set line border
        infoModel.setLayout(new GridBagLayout());
        JLabel lbId = new JLabel("Mã sinh viên:", JLabel.CENTER);
        txtStudentId = new JTextField();
        txtStudentId.setFont(new Font("Tahoma", Font.BOLD, 14));

        JLabel lbTitle = new JLabel("Tiêu đề thu tiền:", JLabel.CENTER);
        txtTitle = new JTextField();
        txtTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbPrice = new JLabel("Tổng tiền:", JLabel.CENTER);
        txtPrice = new JTextField();
        txtPrice.setEnabled(false);
        txtPrice.setEditable(false);
        txtPrice.setText("0");
        txtPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbDate = new JLabel("Ngày:", JLabel.CENTER);
        UtilDateModel modelBillDate = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(modelBillDate, p);
        txtBillDate = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        txtBillDate.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbUtility = new JLabel("Dịch vụ:", JLabel.CENTER);
        cboUtility = new JComboBox<>(displayValueUtility.toArray(new String[0]));
        cboUtility.setFont(new Font("Tahoma", Font.PLAIN, 14));


        List<JComponent> components = Arrays.asList(
                lbId, txtStudentId,
                lbTitle, txtTitle,
                lbPrice, txtPrice,
                lbDate, txtBillDate,
                lbUtility, cboUtility
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


        btnAddUtility = new JButton("Thêm dịch vụ");
        btnAddUtility.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAddUtility.setSize(new Dimension(100, 50));

        btnAddUtility.addActionListener(e -> {
            int index = cboUtility.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn dịch vụ");
                return;
            }
            Utility u = listUtility.get(index);

            if (listUtilitySelected.contains(u)) {
                JOptionPane.showMessageDialog(null, "Dịch vụ đã được chọn");
                return;
            }
            listUtilitySelected.add(u);
            model.addRow(new Object[]{u.getUtilityId(), u.getUtilityName(), u.getPricing()});
            txtPrice.setText(String.valueOf(listUtilitySelected.stream()
                                                               .map(Utility::getPricing)
                                                               .reduce(BigDecimal.ZERO, BigDecimal::add)));
        });

        JPanel btnGroupPanel = new JPanel();
        btnGroupPanel.setPreferredSize(new Dimension(980, 50));
        btnGroupPanel.setBackground(new Color(238, 240, 168));
        btnGroupPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 2;
        c1.gridy = 5;
        c1.gridwidth = 1;
        c1.gridheight = 1;
        c1.fill = GridBagConstraints.BOTH;
        c1.weightx = 1.0;
        c1.weighty = 0.5;
        c1.anchor = GridBagConstraints.CENTER;
        c1.insets = new Insets(5, 5, 30, 5);
        infoModel.add(btnGroupPanel, c1);


        btnGroupPanel.add(btnAddUtility);

        btnCreateBill = new JButton("Tạo hóa đơn");
        btnCreateBill.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCreateBill.setSize(new Dimension(100, 50));
        btnCreateBill.addActionListener(e -> {
            if (txtStudentId.getText().isEmpty() || txtTitle.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin");
                return;
            }
            if (listUtilitySelected.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn dịch vụ");
                return;
            }
            if (StudentDAO.getInstance().findById(txtStudentId.getText()) == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy sinh viên");
                return;
            }

            Invoice invoice = Invoice
                    .builder()
                    .title(txtTitle.getText())
                    .amount(new BigDecimal(txtPrice.getText()))
                    .dueDate((Date) txtBillDate.getModel().getValue())
                    .paymentStatus("pending")
                    .studentId(Integer.parseInt(txtStudentId.getText()))
                    .build();
            invoice.buildInvoiceDetail(listUtilitySelected);

            if (InvoiceDAO.getInstance().save(invoice)) {
                JOptionPane.showMessageDialog(null, "Tạo hóa đơn thành công");
                txtStudentId.setText("");
                txtTitle.setText("");
                txtPrice.setText("0");
                txtBillDate.getModel().setValue(null);
                listUtilitySelected.clear();
                model.setRowCount(0);
            } else {
                JOptionPane.showMessageDialog(null, "Tạo hóa đơn thất bại");
            }
        });
        btnGroupPanel.add(btnCreateBill);

        JButton btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReset.setSize(new Dimension(100, 50));
        btnReset.addActionListener(e -> {
            txtStudentId.setText("");
            txtTitle.setText("");
            txtPrice.setText("0");
            txtBillDate.getModel().setValue(null);
            listUtilitySelected.clear();
            model.setRowCount(0);
        });

        add(infoModel, BorderLayout.WEST);

        // table view
        JPanel showData = new JPanel();
        showData.setPreferredSize(new Dimension(980, 334));
        showData.setBackground(new Color(238, 240, 168));
        showData.setBorder(new LineBorder(new Color(139, 141, 94), 2, true));
        showData.setLayout(new BorderLayout(0, 0));

        model.addColumn("Mã cơ sở vật chất");
        model.addColumn("Tên cơ sở vật chất");
        model.addColumn("Giá");
        model.setRowCount(0);

        JTable table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JTextField()) {
                @Override
                public boolean isCellEditable(EventObject anEvent) {
                    return false;
                }
            });
        }
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
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        showData.add(scrollPane, BorderLayout.CENTER);
        add(showData, BorderLayout.SOUTH);
    }

    private void loadDataToCombobox() {
        listUtility = UtilityDAO.getInstance().findAll();
        displayValueUtility = listUtility.stream().map(Utility::getUtilityName).collect(Collectors.toList());
    }


    private final JTextField txtStudentId, txtTitle, txtPrice;
    private final JButton btnAddUtility;
    private final JButton btnCreateBill;
    private final JComboBox<String> cboUtility;
    private final JDatePickerImpl txtBillDate;
    private List<String> displayValueUtility;
    private List<Utility> listUtility;
    private List<Utility> listUtilitySelected;
}
