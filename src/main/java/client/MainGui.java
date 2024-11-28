package client;

import client.gameMode.ChineseToEnglishGame;
import client.gameMode.EnglishToChineseGame;
import client.gameMode.WordsReview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGui {
    private JFrame f;
    private JButton b1, b2, b3, b4;
    private Client client;

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 700;

    public MainGui(Client client) {
        this.client = client;

        // 创建窗口
        f = new JFrame("单词记忆游戏 - 主界面");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        f.setLocationRelativeTo(null);

        // 使用 GridBagLayout 布局管理器
        f.setLayout(new GridBagLayout());

        // 初始化组件
        initComponent();

        // 显示窗口
        f.setVisible(true);
    }

    private void initComponent() {
        // 创建按钮
        b1 = createButton("根据中文补充英文");
        b2 = createButton("根据英文选择中文");
        b3 = createButton("复习单词");
        b4 = createButton("退出");

        // 添加按钮到窗口
        addComponent(f, b1, 0, 0);
        addComponent(f, b2, 0, 1);
        addComponent(f, b3, 0, 2);
        addComponent(f, b4, 0, 3);

        // 根据中文选英文
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("根据中文补充英文 按钮被点击");
                // 关闭当前窗口
                f.dispose();
                new ChineseToEnglishGame(client);
            }
        });

        // 根据英文选中文
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("根据英文选择中文 按钮被点击");
                // 关闭当前窗口
                f.dispose();
                new EnglishToChineseGame(client);
            }
        });

        // 复习单词
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("复习单词 按钮被点击");
                // 关闭当前窗口
                f.dispose();
                try {
                    new WordsReview(client);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(f, "无法启动复习单词界面: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 退出程序
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("退出 按钮被点击");
                System.exit(0);
            }
        });
    }

    // 按钮的创建函数
    private JButton createButton(String text) {
        return new JButton(text);
    }

    // 添加按钮并绑定对应的事件
    private void addComponent(Container container, Component component, int gridx, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        gbc.insets = new Insets(10, 0, 10, 0); // 上下各10像素的间距
        gbc.gridx = gridx; // 列索引
        gbc.gridy = gridy; // 行索引
        gbc.gridwidth = 1; // 占用1列
        gbc.gridheight = 1; // 占用1行
        gbc.weightx = 1.0; // 水平权重
        gbc.weighty = 1.0; // 垂直权重
        gbc.anchor = GridBagConstraints.CENTER; // 居中对齐

        container.add(component, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client client = new Client();
                new MainGui(client);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "无法启动主界面: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}




