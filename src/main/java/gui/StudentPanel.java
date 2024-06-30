package gui;

import dao.RoomDAO;
import dao.StudentDAO;
import entity.Student;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import utils.Alert;
import utils.GenderUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

@SuppressWarnings("ALL")
public class StudentPanel extends JPanel {
    public StudentPanel() {
        DefaultTableModel model = new DefaultTableModel();
        setPreferredSize(new Dimension(980, 684));
        setBackground(new Color(238, 240, 168));
        setLayout(new BorderLayout(0, 0));

        JPanel infoModel = new JPanel();
        infoModel.setPreferredSize(new Dimension(830, 350));
        infoModel.setBackground(new Color(238, 240, 168));
        infoModel.setBorder(new LineBorder(new Color(139, 141, 94), 2, true)); // set line border
        infoModel.setLayout(new GridBagLayout());
        JLabel lbIdStudent = new JLabel("Mã sinh viên:", JLabel.CENTER);
        txtIdStudent = new JTextField();
        txtIdStudent.setEnabled(false);
        txtIdStudent.setFont(new Font("Tahoma", Font.BOLD, 14));

        JLabel lbFullName = new JLabel("Họ và tên:", JLabel.CENTER);
        txtFullName = new JTextField();
        txtFullName.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbGender = new JLabel("Giới tính:", JLabel.CENTER);
        cboGender = new JComboBox<>();
        cboGender.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cboGender.addItem("Nam");
        cboGender.addItem("Nữ");
        cboGender.addItem("Khác");


        JLabel lbPhone = new JLabel("Số điện thoại:", JLabel.CENTER);
        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbAge = new JLabel("Tuổi:", JLabel.CENTER);
        txtAge = new JTextField();
        txtAge.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbBirthday = new JLabel("Ngày sinh:", JLabel.CENTER);
        UtilDateModel modelBirthday = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(modelBirthday, p);
        txtBirthday = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        txtBirthday.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbAddress = new JLabel("Địa chỉ:", JLabel.CENTER);
        txtAddress = new JTextArea();
        txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lbRoomId = new JLabel("Mã phòng:", JLabel.CENTER);
        txtRoomId = new JTextField();
        txtRoomId.setFont(new Font("Tahoma", Font.PLAIN, 14));

        List<JComponent> components = Arrays.asList(
                lbIdStudent, txtIdStudent,
                lbFullName, txtFullName,
                lbGender, cboGender,
                lbPhone, txtPhone,
                lbAge, txtAge,
                lbBirthday, txtBirthday,
                lbAddress, txtAddress,
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

        setOffModelStudent(infoModel);
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
            addStudent = true;
            setOnModelStudent(infoModel);
            resetTextModelStudent(infoModel);
            txtIdStudent.setText("");
            txtFullName.requestFocus();
            btnSave.setEnabled(true);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        });
        btnEdit.addActionListener(e -> {
            addStudent = false;
            setOnModelStudent(infoModel);
            txtFullName.requestFocus();
            btnSave.setEnabled(true);
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
        });
        btnSave.addActionListener(e -> {
            String fullName = txtFullName.getText();
            String gender = cboGender.getSelectedItem().toString();
            String phone = txtPhone.getText();
            Date birthday = (Date) txtBirthday.getModel().getValue();
            String address = txtAddress.getText();
            Date dob;
            if (fullName.isEmpty()
                    || phone.isEmpty()
                    || birthday == null
                    || txtAge.getText().isEmpty()
                    || address.isEmpty()
                    || txtRoomId.getText().isEmpty()) {
                Alert.showWarning("Vui lòng nhập đầy đủ thông tin");
                return;
            }
            try {
                Integer.parseInt(txtPhone.getText());
                Integer.parseInt(txtAge.getText());
                Integer.parseInt(txtRoomId.getText());
            } catch (NumberFormatException ex) {
                Alert.showWarning("Số điện thoại, tuổi và mã phòng phải là số");
                return;
            }
            int roomId = Integer.parseInt(txtRoomId.getText());
            if (RoomDAO.getInstance().findById(roomId) == null) {
                Alert.showWarning("Mã phòng không tồn tại");
                return;
            }
            int age = Integer.parseInt(txtAge.getText());
            Student student = Student.builder()
                                     .fullName(fullName)
                                     .gender(GenderUtils.getGenderName(gender))
                                     .phone(phone)
                                     .age(age)
                                     .birthday(birthday)
                                     .address(address)
                                     .roomId(roomId)
                                     .build();
            if (addStudent) {
                boolean isSuccess = StudentDAO.getInstance().save(student);
                if (isSuccess) {
                    Alert.showSuccess("Thêm sinh viên thành công");
                    btnReset.doClick();
                } else {
                    Alert.showError("Thêm sinh viên thất bại");
                }
            } else {
                student.setStudentId(Integer.parseInt(txtIdStudent.getText()));
                boolean isSuccess = StudentDAO.getInstance().save(student);
                if (isSuccess) {
                    Alert.showSuccess("Cập nhật sinh viên thành công");
                    btnReset.doClick();
                } else {
                    Alert.showError("Cập nhật sinh viên thất bại");
                }
            }
        });
        btnDelete.addActionListener(e -> {
            int option = Alert.showConfirm("Bạn có chắc chắn muốn xóa sinh viên này không?");
            if (option == JOptionPane.YES_OPTION) {
                // Delete the item
                int id = Integer.parseInt(txtIdStudent.getText());
                boolean isSuccess = StudentDAO.getInstance().delete(id);
                if (isSuccess) {
                    Alert.showSuccess("Xóa sinh viên thành công");
                    btnReset.doClick();
                } else {
                    Alert.showError("Xóa sinh viên thất bại");
                }
            }
        });
        btnReset.addActionListener(e -> {
            // Code to be executed when the button is clicked
            fillDataTableStudent(model);
            txtIdStudent.setText("");
            resetTextModelStudent(infoModel);
            setOffModelStudent(infoModel);
            txtFullName.setFocusable(true);
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
            addStudent = false;
        });

        JPanel showData = new JPanel();
        showData.setPreferredSize(new Dimension(980, 334));
        showData.setBackground(new Color(238, 240, 168));
        showData.setBorder(new LineBorder(new Color(139, 141, 94), 2, true));
        showData.setLayout(new BorderLayout(0, 0));

        model.addColumn("Mã sinh viên");
        model.addColumn("Họ và tên");
        model.addColumn("Giới tính");
        model.addColumn("Số điện thoại");
        model.addColumn("Tuổi");
        model.addColumn("Ngày sinh");
        model.addColumn("Địa chỉ");
        model.addColumn("Mã phòng");
        model.addColumn("Tên phòng");
        fillDataTableStudent(model);

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
                int idmember = Integer.parseInt(table.getValueAt(pos, 0).toString());
                txtIdStudent.setText(table.getValueAt(pos, 0).toString());
                txtFullName.setText(table.getValueAt(pos, 1).toString());
                cboGender.setSelectedItem(table.getValueAt(pos, 2).toString());
                txtPhone.setText(table.getValueAt(pos, 3).toString());
                txtAge.setText(table.getValueAt(pos, 4).toString());

                String[] dateParts = table.getValueAt(pos, 5).toString().split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based in Calendar
                int day = Integer.parseInt(dateParts[2]);

                txtBirthday.getModel().setDate(year, month, day);
                txtBirthday.getModel().setSelected(true);
                txtAddress.setText(table.getValueAt(pos, 6).toString());
                txtRoomId.setText(table.getValueAt(pos, 7).toString());
                setOffModelStudent(infoModel);
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
                addStudent = false;
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        showData.add(scrollPane, BorderLayout.CENTER);

        add(infoModel, BorderLayout.WEST);
        add(grboxButton, BorderLayout.EAST);
        add(showData, BorderLayout.SOUTH);
    }

    private void setOnModelStudent(JPanel infoModel) {
        toggleModelStudent(true);
    }

    private void setOffModelStudent(JPanel infoModel) {
        toggleModelStudent(false);
    }

    private void toggleModelStudent(boolean state) {
        txtIdStudent.setEnabled(false);
        txtFullName.setEnabled(state);
        cboGender.setEnabled(state);
        txtPhone.setEnabled(state);
        txtAge.setEnabled(state);
        txtBirthday.setEnabled(state);
        txtAddress.setEnabled(state);
        txtRoomId.setEnabled(state);
    }

    private void resetTextModelStudent(JPanel infoModel) {
        txtIdStudent.setText("");
        txtFullName.setText("");
        txtPhone.setText("");
        txtAge.setText("");
        txtAddress.setText("");
        txtRoomId.setText("");
        txtBirthday.getModel().setValue(null);
    }

    private void fillDataTableStudent(DefaultTableModel model) {
        model.setRowCount(0);
        for (Student b : StudentDAO.getInstance().findAll()) {
            Object[] data = new Object[15];
            data[0] = b.getStudentId();
            data[1] = b.getFullName();
            data[2] = GenderUtils.convertGenderName(b.getGender());
            data[3] = b.getPhone();
            data[4] = b.getAge();
            data[5] = b.getBirthday();
            data[6] = b.getAddress();
            data[7] = b.getRoomId();
            data[8] = b.getRoom().getRoomName();
            model.addRow(data);
        }
    }

    private boolean addStudent = false;
    private final JTextField txtIdStudent;
    private final JTextField txtFullName;
    private final JComboBox<String> cboGender;
    private final JTextField txtPhone;
    private final JTextField txtAge;
    private final JDatePickerImpl txtBirthday;
    private final JTextArea txtAddress;
    private final JTextField txtRoomId;
    private final JButton btnAdd;
    private final JButton btnEdit;
    private final JButton btnSave;
    private final JButton btnDelete;
    private final JButton btnReset;
}
