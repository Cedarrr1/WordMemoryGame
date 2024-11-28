package tool;


import tool.ScoreManager;

import javax.swing.*;
import java.awt.*;

public class HealthBar extends JPanel {
    private ScoreManager scoreManager;

    public HealthBar(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        setPreferredSize(new Dimension(50, 200)); // 设置固定宽度和高度
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int maxHP = scoreManager.getMaxHP();
        int currentHP = scoreManager.getHp();

        // 绘制满生命值的背景矩形
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 计算当前生命值的高度
        int healthHeight;
        if(currentHP >= 10) {
            healthHeight = (int) ((double) 10 / maxHP * getHeight());
        } else {
            healthHeight = (int) ((double) currentHP / maxHP * getHeight());
        }

        // 绘制当前生命值的红色矩形
        g.setColor(Color.RED);
        g.fillRect(0, getHeight() - healthHeight, getWidth(), healthHeight);
    }

    public void updateHealthBar() {
        repaint(); // 刷新健康条
    }
}




