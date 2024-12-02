package client.gameMode;

import client.Client;
import tool.FileUtil;
import tool.CountdownTimer;

import javax.swing.*;

public class EnglishToChineseGame extends GameGui {

    public EnglishToChineseGame(Client client) {
        super(GameMode.ENGLISH_TO_CHINESE, client);
        // 设置回调函数 倒计时结束时调用
        countdownTimer.setOnTimeOverCallback(() -> handleTimeOver());
    }


    @Override
    protected void initializeButtonListeners() {
        a.addActionListener(e -> checkAnswer("A"));
        b.addActionListener(e -> checkAnswer("B"));
        c.addActionListener(e -> checkAnswer("C"));
        d.addActionListener(e -> checkAnswer("D"));
    }

    /**
     * 检查用户选择的答案是否正确
     * @param selectedOption 用户选择的选项
     */
    private void checkAnswer(String selectedOption) {
        String userAnswer = meanings.get(getButtonIndex(selectedOption));
        boolean isCorrect = userAnswer.equals(correctMeaning);

        if (isCorrect) {
            JOptionPane.showMessageDialog(j, "回答正确！");
            FileUtil.saveMasteredWord(currentWord, correctMeaning);
        } else {
            JOptionPane.showMessageDialog(j, "回答错误，正确答案是：" + correctMeaning);
            FileUtil.saveUnmasteredWord(currentWord, correctMeaning, "作答错误");
        }

        // 管理得分和hp
        updateScoreAndHp(isCorrect, true);

        countdownTimer.stop(); // 停止计时器
        fetchRandomWord();
    }

    /**
     *
     * @param option
     * @return 案件-索引的映射
     * 无效则返回 -1
     */
    private int getButtonIndex(String option) {
        switch (option) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            default:
                return -1;
        }
    }

    /**
     * 初始化游戏逻辑
     */
    private void handleTimeOver() {
        JOptionPane.showMessageDialog(j, "您没有回答，正确答案是：" + correctMeaning);
        FileUtil.saveUnmasteredWord(currentWord, correctMeaning, "未作答");
        updateScoreAndHp(false, false); // 处理未作答的情况

        countdownTimer.stop(); // 停止计时器
        fetchRandomWord(); // 获取新题目并重启计时器
    }
}