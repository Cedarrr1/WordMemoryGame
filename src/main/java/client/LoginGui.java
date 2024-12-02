package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGui extends JFrame {
    private JButton loginButton;
    private JButton exitButton;
    private JTextField usernameField;
    private JPasswordField passwordField;;

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 700;
    private Client client;

    public LoginGui(Client client) {
        this.client = client;

        // 初始化窗口
        setTitle("单词记忆游戏 - 登录页面");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // 创建游戏名标签
        JLabel gameTitleLabel = new JLabel("单词记忆大挑战", SwingConstants.CENTER);
        Font titleFont = new Font("SimSun", Font.BOLD, 24); // 使用支持中文的字体
        gameTitleLabel.setFont(titleFont);
        gameTitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 创建登录面板
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 创建用户名和密码输入框
        JLabel usernameLabel = new JLabel("用户名:", SwingConstants.RIGHT);
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("密码:", SwingConstants.RIGHT);
        passwordField = new JPasswordField(20);

        // 创建按钮
        loginButton = new JButton("登录");
        exitButton = new JButton("退出");

        // 添加按钮监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        // 添加组件到登录面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(exitButton, gbc);

        // 添加游戏名标签和登录面板到主面板
        mainPanel.add(gameTitleLabel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);

        // 添加主面板到窗口
        add(mainPanel);

        // 显示窗口
        setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        // 向服务器发送登录请求
        client.sendLoginRequest(username, password, this);
    }

    public void showSuccessMessage() {
        dispose(); // 关闭当前窗口
        new MainGui(client); // 打开 MainGui 窗口
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        //分发线程
        SwingUtilities.invokeLater(() -> {
            try {
                Client client = new Client();
                new LoginGui(client);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "无法启动主界面: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}










