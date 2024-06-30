package gui;

import java.awt.*;

import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class LoginFrame extends JFrame {

    final LoginFrame flogin = this;
    private static final long serialVersionUID = 1L;


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoginFrame() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);

        getContentPane().setLayout(new BorderLayout(0, 0));

        // Header
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(1000, 50));
        header.setLayout(new BorderLayout(0, 0));
        JPanel pnExit = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) pnExit.getLayout();
        flowLayout_1.setVgap(10);
        flowLayout_1.setHgap(10);
        pnExit.setPreferredSize(new Dimension(50, 50));
        pnExit.setBackground(new Color(246, 247, 246));
        JLabel lbExit = new JLabel("", JLabel.CENTER);
        lbExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] ObjButtons = {"Có", "Không"};
                int PromptResult = JOptionPane.showOptionDialog(
                        null,
                        "Bạn có muốn thoát không?",
                        "Quản Lý Ký Túc Xá",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        ObjButtons,
                        ObjButtons[1]
                );
                if (PromptResult == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pnExit.setBackground(new Color(254, 254, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pnExit.setBackground(new Color(246, 247, 246));
            }
        });
        lbExit.setIcon(new ImageIcon(Objects.requireNonNull(LoginFrame.class.getResource("/icon/close.png"))));

        pnExit.add(lbExit);
        header.add(pnExit, BorderLayout.EAST);
        header.setBackground(new Color(246, 247, 246));
        getContentPane().add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setPreferredSize(new Dimension(1000, 400));
        content.setBackground(new Color(246, 247, 246));
        content.setLayout(new BorderLayout(0, 0));

        // panel contain image book
        JPanel hero = new JPanel();
        FlowLayout flowLayout = (FlowLayout) hero.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        hero.setPreferredSize(new Dimension(400, 400));
        hero.setBackground(new Color(246, 247, 246));
        JLabel lbhero = new JLabel("", JLabel.CENTER);

        // fit image to scale base panel
        lbhero.setIcon(
                new ImageIcon(
                        new ImageIcon(Objects.requireNonNull(
                                LoginFrame
                                        .class
                                        .getResource("/icon/bg_login.jpeg")))
                                .getImage()
                                .getScaledInstance(
                                        400,
                                        400,
                                        Image.SCALE_DEFAULT
                                )));

        hero.add(lbhero);
        content.add(hero, BorderLayout.WEST);

        // panel contain content login
        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(550, 400));
        container.setBackground(new Color(254, 254, 255));
        container.setLayout(new BorderLayout(0, 0));

        // set border shadow
        DropShadowBorder shadow = new DropShadowBorder();
        shadow.setShadowColor(new Color(246, 247, 246));
        shadow.setShowLeftShadow(true);
        shadow.setShowRightShadow(true);
        shadow.setShowBottomShadow(true);
        shadow.setShowTopShadow(true);

        JPanel container1 = new JPanel();
        container1.setPreferredSize(new Dimension(550, 350));
        container1.setBackground(new Color(254, 254, 255));
        container1.setLayout(new BorderLayout(0, 0));
        JLabel lbTitle = new JLabel("Đăng nhập", JLabel.CENTER);
        lbTitle.setFont(new Font("Tahoma", Font.PLAIN, 30));

        // contain component username, password, button login, sign up
        JPanel container2 = new JPanel();
        container2.setPreferredSize(new Dimension(550, 300));
        container2.setBackground(new Color(254, 254, 255));
        container2.setLayout(new BoxLayout(container2, BoxLayout.Y_AXIS));
        container2.add(Box.createVerticalGlue());

        JPanel pnUsername = new JPanel();
        pnUsername.setPreferredSize(new Dimension(550, 30));
        pnUsername.setBackground(new Color(254, 254, 255));
        pnUsername.setLayout(new BoxLayout(pnUsername, BoxLayout.X_AXIS));
        pnUsername.add(Box.createHorizontalGlue());
        JLabel lbUsername = new JLabel("Tên đăng nhập:", JLabel.LEFT);
        lbUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        JTextField txtusername = new JTextField();
        pnUsername.add(Box.createRigidArea(new Dimension(80, 0)));
        pnUsername.add(lbUsername);
        pnUsername.add(Box.createRigidArea(new Dimension(20, 0)));
        pnUsername.add(txtusername);
        pnUsername.add(Box.createRigidArea(new Dimension(80, 0)));

        JPanel pnPassword = new JPanel();
        pnPassword.setPreferredSize(new Dimension(550, 30));
        pnPassword.setBackground(new Color(254, 254, 255));
        pnPassword.setLayout(new BoxLayout(pnPassword, BoxLayout.X_AXIS));
        pnPassword.add(Box.createHorizontalGlue());
        JLabel lbPassword = new JLabel("Mật khẩu:", JLabel.LEFT);
        lbPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        JPasswordField txtPassword = new JPasswordField();
        pnPassword.add(Box.createRigidArea(new Dimension(80, 0)));
        pnPassword.add(lbPassword);
        pnPassword.add(Box.createRigidArea(new Dimension(60, 0)));
        pnPassword.add(txtPassword);
        pnPassword.add(Box.createRigidArea(new Dimension(80, 0)));

        JPanel pnButtonLogin = new JPanel();
        pnButtonLogin.setPreferredSize(new Dimension(550, 30));
        pnButtonLogin.setBackground(new Color(254, 254, 255));
        // pnButtonLogin.setBackground(new Color(234, 234, 255));
        pnButtonLogin.setLayout(new BoxLayout(pnButtonLogin, BoxLayout.X_AXIS));
        pnButtonLogin.add(Box.createHorizontalGlue());
        JButton btnLogin = new JButton("Sign in");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed();
            }

            private void btnLoginActionPerformed() {
                AdminMainFrame adFrame = new AdminMainFrame(flogin);
                adFrame.setVisible(true);
                //                EventQueue.invokeLater(() -> {
                //                    String user = txtusername.getText();
                //                    String pass = String.valueOf(txtPassword.getPassword());
                //                    boolean check = "admin".equals(user) && "admin".equals(pass);
                //                    if (check) {
                //                        AdminMainFrame adFrame = new AdminMainFrame(flogin);
                //                        adFrame.setVisible(true);
                //                    } else {
                //                        JOptionPane.showMessageDialog(
                //                                flogin, "Invalid username or password", "About",
                //                                JOptionPane.INFORMATION_MESSAGE
                //                        );
                //                    }
                //                });
            }
        });

        btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnLogin.setBackground(new Color(25, 180, 76));
        btnLogin.setForeground(Color.white);
        btnLogin.setPreferredSize(new Dimension(100, 30));
        pnButtonLogin.add(btnLogin);
        pnButtonLogin.add(Box.createRigidArea(new Dimension(205, 0)));

        container2.add(Box.createRigidArea(new Dimension(0, 50)));
        container2.add(pnUsername);
        container2.add(Box.createRigidArea(new Dimension(0, 20)));
        container2.add(pnPassword);
        container2.add(Box.createRigidArea(new Dimension(0, 50)));
        container2.add(pnButtonLogin);
        container2.add(Box.createRigidArea(new Dimension(0, 65)));

        container1.add(container2, BorderLayout.CENTER);
        container1.add(lbTitle, BorderLayout.NORTH);
        container1.setBorder(shadow);
        container.add(container1, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        bottom.setPreferredSize(new Dimension(550, 50));
        bottom.setBackground(new Color(246, 247, 246));
        container.add(bottom, BorderLayout.SOUTH);
        JPanel right = new JPanel();
        right.setPreferredSize(new Dimension(50, 400));
        right.setBackground(new Color(246, 247, 246));
        content.add(right, BorderLayout.EAST);
        content.add(container, BorderLayout.CENTER);
        getContentPane().add(content, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnLogin);
        pack();
        setLocationRelativeTo(null);
    }

}
