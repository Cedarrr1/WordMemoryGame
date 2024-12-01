package client.gameMode;

import client.MainGui;
import tool.CountdownTimer;
import tool.HealthBar;
import tool.ScoreManager;
import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class GameGui {
    protected JFrame j;
    protected JPanel engWordPanel, centerPanel, timerPanel, inputPanel, choicesPanel;
    protected JLabel engWordLabel, timerLabel, scoreLabel, hpLabel;
    private HealthBar hpBar;
    protected JButton a, b, c, d;
    protected JTextField inputField;
    protected JButton submitButton;
    protected JButton quitButton;
    protected String currentWord;
    protected String correctMeaning;
    protected String currentMeaning;
    protected List<String> meanings;
    protected Client client;

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 700;

    // 新增游戏模式枚举
    public enum GameMode {
        ENGLISH_TO_CHINESE,
        CHINESE_TO_ENGLISH
    }


    public GameGui() {

    }

    protected GameMode gameMode;

    // 定时器相关变量
    protected CountdownTimer countdownTimer;

    // 分数管理相关变量
    protected ScoreManager scoreManager;
    public GameGui(GameMode mode, Client client) {
        this.gameMode = mode;
        this.client = client;

        // 初始化窗口
        j = new JFrame("Game page");
        j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        j.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        j.setLocationRelativeTo(null);

        // 初始化分数管理器
        scoreManager = new ScoreManager(0, 10); // 初始分数为0，初始生命值为10
        hpBar = new HealthBar(scoreManager);

        engWordPanel = createEngWordPanel();
        timerPanel = createTimerPanel();

        // 根据游戏模式创建不同的中心面板
        if (gameMode == GameMode.ENGLISH_TO_CHINESE) {
            choicesPanel = createChoicesPanel();
            centerPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 5);
            centerPanel.add(timerPanel, gbc);
            gbc.gridy = 1;
            centerPanel.add(choicesPanel, gbc);
        } else if (gameMode == GameMode.CHINESE_TO_ENGLISH) {
            inputPanel = createInputPanel();
            centerPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 5);
            centerPanel.add(timerPanel, gbc);
            gbc.gridy = 1;
            centerPanel.add(inputPanel, gbc);
        }

        //退出按钮
        quitButton = new JButton("退出");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭当前窗口
                SwingUtilities.invokeLater(() -> new MainGui(client));
            }
        });
        j.add(quitButton, BorderLayout.SOUTH);

        // 添加面板到窗口
        j.add(engWordPanel, BorderLayout.NORTH);
        j.add(centerPanel, BorderLayout.CENTER);
        j.add(hpBar, BorderLayout.EAST);

        // 初始化按钮监听器
        initializeButtonListeners();

        // 初始化游戏逻辑
        initializeGameLogic();

        // 显示窗口
        j.setVisible(true);
    }

    // 创建显示一个选取英文单词的panel
    protected JPanel createEngWordPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 添加水平和垂直间距

        // 添加英文单词的显示组件
        engWordLabel = new JLabel("单词: ");
        engWordLabel.setFont(new Font("SimSun", Font.BOLD, 18)); // 设置字体
        panel.add(engWordLabel);

        return panel;
    }

    // 创建显示倒计时的panel
    protected JPanel createTimerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 添加水平和垂直间距

        // 添加计时器的显示组件
        timerLabel = new JLabel("时间: 00:00");
        timerLabel.setFont(new Font("SimSun", Font.BOLD, 16)); // 设置字体
        panel.add(timerLabel);

        // 添加分数和生命值显示
        scoreLabel = new JLabel("分数: " + scoreManager.getScore());
        scoreLabel.setFont(new Font("SimSun", Font.BOLD, 16)); // 设置字体
        panel.add(scoreLabel);

        hpLabel = new JLabel("生命值: " + scoreManager.getHp());
        hpLabel.setFont(new Font("SimSun", Font.BOLD, 16)); // 设置字体
        panel.add(hpLabel);

        // 初始化定时器
        countdownTimer = new CountdownTimer(10, timerLabel);

        return panel;
    }

    // 根据英文单词选择中文解释时，创建显示四个中文解释选项的panel
    protected JPanel createChoicesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 5)); // 使用GridLayout布局，添加水平和垂直间距

        // 添加四个中文选项按钮
        a = new JButton("A");
        b = new JButton("B");
        c = new JButton("C");
        d = new JButton("D");

        a.setFont(new Font("SimSun", Font.PLAIN, 12)); // 设置字体
        b.setFont(new Font("SimSun", Font.PLAIN, 12));
        c.setFont(new Font("SimSun", Font.PLAIN, 12));
        d.setFont(new Font("SimSun", Font.PLAIN, 12));

        panel.add(a);
        panel.add(b);
        panel.add(c);
        panel.add(d);

        return panel;
    }

    // 创建输入框和提交按钮的panel
    protected JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // 创建带有下划线提示的输入框
        inputField = new JTextField(20);
        inputField.setEditable(true); // 允许编辑

        // 创建提交按钮
        submitButton = new JButton("提交");

        panel.add(inputField);
        panel.add(submitButton);

        return panel;
    }


    // 初始化按钮监听器
    protected abstract void initializeButtonListeners();

    // 抽象方法，用于初始化游戏逻辑
    // 在初始化逻辑中加入对游戏模式的判断
    protected void initializeGameLogic() {
        switch (gameMode) {
            case ENGLISH_TO_CHINESE:
                fetchRandomWord();
                break;
            case CHINESE_TO_ENGLISH:
                fetchRandomChineseExplanation();
                break;
            default:
                throw new IllegalArgumentException("未知的游戏模式");
        }
    }

    // 获取数据库连接
    protected Connection getDatabaseConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/vocabulary_db";
        String user = "root";
        String password = "wod.1314";
        return DriverManager.getConnection(url, user, password);
    }

    // 根据英文单词选取汉语解释
    //从数据库中随机抽取一个单词
    protected void fetchRandomWord() {
        try (Connection conn = getDatabaseConnection()) {
            String sql = "SELECT enS, mean FROM words ORDER BY RAND() LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                currentWord = rs.getString("enS");
                correctMeaning = rs.getString("mean");
                engWordLabel.setText("单词: " + currentWord);
                setMeaningOptions();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 重置并启动倒计时
        if(scoreManager.isGameOver()) {
            countdownTimer.stop();
            countdownTimer.reset();
            dispose();
        } else {
            countdownTimer.start();
        }
    }

    // 设置选项按钮的文本
    protected void setMeaningOptions() {
        meanings = new ArrayList<>();
        meanings.add(correctMeaning);

        // 从数据库中随机抽取三个其他意思
        try (Connection conn = getDatabaseConnection()) {
            String sql = "SELECT mean FROM words WHERE mean != ? ORDER BY RAND() LIMIT 3";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correctMeaning);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                meanings.add(rs.getString("mean"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 打乱选项顺序
        Collections.shuffle(meanings);

        // 设置按钮文本
        a.setText("A. " + meanings.get(0));
        b.setText("B. " + meanings.get(1));
        c.setText("C. " + meanings.get(2));
        d.setText("D. " + meanings.get(3));
    }

    // 从数据库中随机抽取一个中文解释并设置为题目
    protected void fetchRandomChineseExplanation() {
        try (Connection conn = getDatabaseConnection()) {
            String sql = "SELECT enS, mean FROM words ORDER BY RAND() LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                currentWord = rs.getString("enS");
                currentMeaning = rs.getString("mean");

                // 显示单词的首尾字母，并用下划线表示需要填写的部分，下划线之间用空格分隔
                StringBuilder maskedWord = new StringBuilder();
                maskedWord.append(currentWord.charAt(0));
                for (int i = 1; i < currentWord.length() - 1; i++) {
                    maskedWord.append("_ ");
                }
                maskedWord.append(currentWord.charAt(currentWord.length() - 1));

                engWordLabel.setText("<html><div style='text-align:center;'>中文解释: " + currentMeaning + "<br>" + maskedWord.toString() + "</div></html>");
                inputField.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 重置并启动倒计时
        if(scoreManager.isGameOver()) {
            countdownTimer.stop();
            countdownTimer.reset();
            dispose();
        } else {
            countdownTimer.start();
        }
    }

    // 更新分数和生命值的方法
    protected void updateScoreAndHp(boolean isCorrect, boolean hasSubmitted) {
        if (isCorrect && hasSubmitted) {
            scoreManager.addScore(1);
            scoreManager.deductHp(-1); // 相当于hp增加
        } else if (!isCorrect && hasSubmitted) {
            scoreManager.deductScore(2);
            scoreManager.deductHp(2);
        } else if (!hasSubmitted) {
            scoreManager.deductScore(1);
            scoreManager.deductHp(1);
        }

        // 更新分数和生命值标签
        if (scoreLabel != null) {
            scoreLabel.setText("分数: " + scoreManager.getScore());
        }
        if (hpLabel != null) {
            hpLabel.setText("生命值: " + scoreManager.getHp());
        }
        // 更新hp条
        hpBar.updateHealthBar();

        // 检查是否游戏结束
        if (scoreManager.isGameOver()) {
            JOptionPane.showMessageDialog(j, "游戏结束！您的最终分数是: " + scoreManager.getScore());
            countdownTimer.reset();
            j.dispose(); // 关闭当前窗口
            MainGui.main(new String[]{}); // 返回主界面
        } else {
            // 获取新题目并重启计时器
            initializeGameLogic();
        }
    }


    protected void dispose() {
        countdownTimer.stop();
        countdownTimer.reset();
        j.dispose();
    }
}