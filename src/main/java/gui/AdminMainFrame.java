package gui;

import dao.InvoiceDAO;
import dao.RoomDAO;
import dao.StudentDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Map;

@SuppressWarnings("ALL")
public class AdminMainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel container;
    private final JLabel lbTitleContainer;

    JPanel managementStudents() {
        return new StudentPanel();
    }

    static void initCRUDComponents(JPanel jpncontainer) {
        jpncontainer.setLayout(new GridLayout(6, 1, 12, 12));
        JButton btnAdd = new JButton("Thêm", new ImageIcon(AdminMainFrame.class.getResource("/icon/add.png")));
        btnAdd.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnAdd.setIconTextGap(20);
        JButton btnEdit = new JButton("Sửa", new ImageIcon(AdminMainFrame.class.getResource("/icon/edit.png")));
        btnEdit.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnEdit.setIconTextGap(20);
        JButton btnDelete = new JButton("Xóa", new ImageIcon(AdminMainFrame.class.getResource("/icon/delete.png")));
        btnDelete.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnDelete.setIconTextGap(20);
        JButton btnSave = new JButton("Lưu", new ImageIcon(AdminMainFrame.class.getResource("/icon/save.png")));
        btnSave.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnSave.setIconTextGap(20);
        JButton btnReset = new JButton("Làm mới", new ImageIcon(AdminMainFrame.class.getResource("/icon/reset.png")));
        btnReset.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnReset.setIconTextGap(10);

        jpncontainer.add(btnAdd);
        jpncontainer.add(btnEdit);
        jpncontainer.add(btnSave);
        jpncontainer.add(btnDelete);
        jpncontainer.add(btnReset);
    }

    JPanel managementRooms() {
        return new RoomPanel();
    }

    JPanel managementStatistics() {
        JPanel statistics = new JPanel();
        statistics.setPreferredSize(new Dimension(980, 684));
        statistics.setBackground(new Color(238, 240, 168));
        statistics.setLayout(new BoxLayout(statistics, BoxLayout.Y_AXIS));

        JLabel lbCountMember = new JLabel(MessageFormat.format(
                "Số lượng sinh viên: {0}",
                StudentDAO.getInstance().countStudent()
        ), JLabel.CENTER);
        lbCountMember.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lbCountMember.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbCountRoom = new JLabel(
                MessageFormat.format("Số lượng phòng: {0}", RoomDAO.getInstance().count()),
                JLabel.CENTER
        );
        lbCountRoom.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lbCountRoom.setAlignmentX(Component.CENTER_ALIGNMENT);

        Map<String, BigDecimal> revenue = InvoiceDAO.getInstance().calculateRevenue();

        JLabel lbCountLoan = new JLabel(MessageFormat.format(
                "Doanh thu đã thu tiền: {0}",
                revenue.get("revenue")
        ), JLabel.CENTER);
        lbCountLoan.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lbCountLoan.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbCountLoanNotPaid = new JLabel(MessageFormat.format(
                "Doanh thu chưa thu tiền: {0}",
                revenue.get("revenueNotPaid")
        ), JLabel.CENTER);
        lbCountLoanNotPaid.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lbCountLoanNotPaid.setAlignmentX(Component.CENTER_ALIGNMENT);


        statistics.add(Box.createRigidArea(new Dimension(0, 250)));
        statistics.add(lbCountMember);
        statistics.add(lbCountRoom);
        statistics.add(lbCountLoan);
        statistics.add(lbCountLoanNotPaid);
        return statistics;
    }

    public AdminMainFrame(LoginFrame flogin) {
        flogin.setVisible(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        getContentPane().setLayout(new BorderLayout(0, 0));
        container = new JPanel();
        container.setBorder(new EmptyBorder(8, 8, 8, 8));
        container.setLayout(new BorderLayout(0, 0));
        container.setPreferredSize(new Dimension(1000, 700));
        // Jpanel slider
        JPanel pnSlide = new JPanel();
        pnSlide.setBackground(Color.LIGHT_GRAY);
        pnSlide.setPreferredSize(new Dimension(200, 700));
        getContentPane().add(pnSlide, BorderLayout.WEST);
        pnSlide.setLayout(new GridLayout(13, 1, 8, 8)); // 18 rows, 1 column, vgap hgap : 8px
        JButton btnRoom = new JButton("Quản lý phòng ktx");
        btnRoom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                container.removeAll();
                container.add(lbTitleContainer, BorderLayout.NORTH);
                container.add(managementRooms(), BorderLayout.CENTER);
                container.repaint();
                container.revalidate();
                lbTitleContainer.setText("Quản lý phòng ktx");
            }
        });
        JButton btnStudent = new JButton("Quản lý sinh viên");
        btnStudent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                container.removeAll();
                container.add(lbTitleContainer, BorderLayout.NORTH);
                container.add(managementStudents(), BorderLayout.CENTER);
                container.repaint();
                container.revalidate();
                lbTitleContainer.setText("Quản lý sinh viên");
            }
        });
        JButton btnUtility = new JButton("Quản lý cơ sở vật chất");
        btnUtility.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                container.removeAll();
                container.repaint();
                container.revalidate();
                container.add(lbTitleContainer, BorderLayout.NORTH);
                container.add(new UtilityPanel(), BorderLayout.CENTER);
                lbTitleContainer.setText("Quản lý cơ sở vật chất");
            }
        });
        JButton btnOrder = new JButton("Lập hóa đơn");
        btnOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                container.removeAll();
                container.repaint();
                container.revalidate();
                container.add(lbTitleContainer, BorderLayout.NORTH);
                container.add(new CreateBillPanel(), BorderLayout.CENTER);
                lbTitleContainer.setText("Lập hóa đơn");
            }
        });

        JButton btnBill = new JButton("Quản lý hóa đơn");
        btnBill.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                container.removeAll();
                container.repaint();
                container.revalidate();
                container.add(lbTitleContainer, BorderLayout.NORTH);
                container.add(new BillPanel(), BorderLayout.CENTER);
                lbTitleContainer.setText("Quản lý hóa đơn");
            }
        });

        JButton btnStatistics = new JButton("Thống kê");
        btnStatistics.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                container.removeAll();
                container.add(lbTitleContainer, BorderLayout.NORTH);
                container.add(managementStatistics(), BorderLayout.CENTER);
                container.repaint();
                container.revalidate();
                lbTitleContainer.setText("Thống kê");
            }
        });
        pnSlide.add(btnRoom);
        pnSlide.add(btnStudent);
        pnSlide.add(btnUtility);
        pnSlide.add(btnOrder);
        pnSlide.add(btnBill);
        pnSlide.add(btnStatistics);

        for (int i = 7; i < 13; i++) {
            JPanel tmp = new JPanel();
            tmp.setBackground(Color.LIGHT_GRAY);
            pnSlide.add(tmp);
        }
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        pnSlide.add(btnLogout);
        // JPanel header
        JPanel pnHeader = new JPanel();
        pnHeader.setBackground(Color.LIGHT_GRAY);
        pnHeader.setPreferredSize(new Dimension(1200, 50));
        getContentPane().add(pnHeader, BorderLayout.NORTH);
        pnHeader.setLayout(new BorderLayout(0, 0));
        JPanel pnExit = new JPanel();
        FlowLayout flowLayout = (FlowLayout) pnExit.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        pnExit.setBackground(Color.LIGHT_GRAY);
        pnExit.setPreferredSize(new Dimension(50, 50));
        pnHeader.add(pnExit, BorderLayout.EAST);
        JLabel lbExit = new JLabel("", JLabel.CENTER);
        lbExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pnExit.setBackground(Color.gray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pnExit.setBackground(Color.LIGHT_GRAY);
            }
        });
        JLabel lbTitle = new JLabel("QUẢN LÝ KTX", JLabel.CENTER);
        lbTitle.setForeground(Color.BLUE);
        lbTitle.setFont(new Font("Tahoma", Font.PLAIN, 40));
        lbTitle.setPreferredSize(new Dimension(1150, 50));
        pnHeader.add(lbTitle, BorderLayout.CENTER);

        lbExit.setPreferredSize(new Dimension(50, 50));
        lbExit.setIcon(new ImageIcon(AdminMainFrame.class.getResource("/icon/close.png")));
        pnExit.add(lbExit);

        // JPanel container

        getContentPane().add(container, BorderLayout.CENTER);
        lbTitleContainer = new JLabel("Quản lý phòng ktx", JLabel.CENTER);
        lbTitleContainer.setForeground(Color.BLUE);
        lbTitleContainer.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lbTitleContainer.setPreferredSize(new Dimension(1000, 30));
        container.add(lbTitleContainer, BorderLayout.NORTH);
        container.setBackground(new Color(238, 240, 168));
        container.add(new CreateBillPanel(), BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                flogin.setVisible(true);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

}
