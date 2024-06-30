package gui;

import dao.InvoiceDAO;
import dao.UtilityDAO;
import entity.Invoice;
import entity.InvoiceDetail;
import entity.Utility;
import org.jdatepicker.impl.JDatePickerImpl;
import utils.Alert;
import utils.BillStatusUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;

public class BillPanel extends JPanel {

    public BillPanel() {
        setPreferredSize(new Dimension(980, 684));
        setBackground(new Color(238, 240, 168));
        setLayout(new BorderLayout(0, 0));
        loadListInvoice();
        fillDataTableInvoice();
        JPanel listInvoice = new JPanel();
        listInvoice.setPreferredSize(new Dimension(980, 350));
        listInvoice.setBackground(new Color(238, 240, 168));
        listInvoice.setBorder(new LineBorder(new Color(139, 141, 94), 2, true));
        listInvoice.setLayout(new BorderLayout(0, 0));
        JTable table = new JTable(model);
        configTable(table);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int pos = table.rowAtPoint(e.getPoint());
                Integer id = (Integer) table.getValueAt(pos, 0);
                invoiceId = id;
                fillDetailInvoice(id);
            }
        });
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        listInvoice.add(scrollPane, BorderLayout.CENTER);
        add(listInvoice, BorderLayout.NORTH);

        JPanel btnGroup = new JPanel();
        btnGroup.setPreferredSize(new Dimension(980, 50));
        btnGroup.setBackground(new Color(238, 240, 168));
        btnGroup.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton btnPaid = new JButton("Đã thanh toán");
        btnPaid.setPreferredSize(new Dimension(150, 40));
        btnPaid.setBackground(new Color(139, 141, 94));
        btnPaid.setForeground(Color.WHITE);
        btnPaid.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGroup.add(btnPaid);
        btnPaid.addActionListener(e -> update("PAID"));

        JButton btnFree = new JButton("Miễn phí");
        btnFree.setPreferredSize(new Dimension(150, 40));
        btnFree.setBackground(new Color(139, 141, 94));
        btnFree.setForeground(Color.WHITE);
        btnFree.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGroup.add(btnFree);

        btnFree.addActionListener(e -> update("FREE"));

        add(btnGroup, BorderLayout.CENTER);

        JPanel detailInvoice = new JPanel();
        loadDetailInvoice();
        detailInvoice.setPreferredSize(new Dimension(980, 250));
        detailInvoice.setBackground(new Color(238, 240, 168));
        detailInvoice.setBorder(new LineBorder(new Color(139, 141, 94), 2, true));
        detailInvoice.setLayout(new BorderLayout(0, 0));
        JTable table1 = new JTable(model1);
        configTable(table1);
        // Add the table to a scroll pane
        JScrollPane scrollPane1 = new JScrollPane(table1);
        detailInvoice.add(scrollPane1, BorderLayout.CENTER);
        add(detailInvoice, BorderLayout.SOUTH);
    }

    void update(String status) {
        if (invoiceId != null) {
            boolean success = InvoiceDAO.getInstance().update(invoiceId, status);
            if (success) {
                fillDataTableInvoice();
                model1.setRowCount(0);
                Alert.showSuccess("Cập nhật trạng thái hóa đơn thành công!");
            } else {
                Alert.showError("Cập nhật hóa đơn thất bại");
            }
        } else {
            Alert.showWarning("Vui lòng chọn hóa đơn!");
        }
    }

    void fillDetailInvoice(Integer invoiceId) {
        model1.setRowCount(0);
        List<InvoiceDetail> list = InvoiceDAO.getInstance().findInvoiceDetailById(invoiceId);
        for (InvoiceDetail b : list) {
            Object[] data = new Object[15];
            data[0] = b.getInvoiceId();
            data[1] = b.getUtilityId();
            data[2] = b.getUtility().getUtilityName();
            data[3] = b.getUtility().getPricing();
            model1.addRow(data);
        }
    }

    void configTable(JTable table) {
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
    }

    void loadListInvoice() {
        model.addColumn("Mã hóa đơn");
        model.addColumn("Mã sinh viên");
        model.addColumn("Tên sinh viên");
        model.addColumn("Mã phòng");
        model.addColumn("Tên hóa đơn");
        model.addColumn("Số tiền");
        model.addColumn("Ngày đến hạn");
        model.addColumn("Trạng thái thanh toán");
    }

    void loadDetailInvoice() {
        model1.addColumn("Mã hóa đơn");
        model1.addColumn("Mã tiện ích");
        model1.addColumn("Tên tiện ích");
        model1.addColumn("Thành tiền");
    }


    private void fillDataTableInvoice() {
        model.setRowCount(0);
        List<Invoice> list = InvoiceDAO.getInstance().findAll();
        for (Invoice b : list) {
            Object[] data = new Object[15];
            data[0] = b.getInvoiceId();
            data[1] = b.getStudentId();
            data[2] = b.getStudent().getFullName();
            data[3] = b.getStudent().getRoomId();
            data[4] = b.getTitle();
            data[5] = b.getAmount();
            data[6] = b.getDueDate();
            data[7] = BillStatusUtils.convertBillStatus(b.getPaymentStatus());
            model.addRow(data);
        }
    }

    private Integer invoiceId;
    private DefaultTableModel model = new DefaultTableModel();
    private DefaultTableModel model1 = new DefaultTableModel();
}
