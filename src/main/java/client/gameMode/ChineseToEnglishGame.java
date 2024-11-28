package client.gameMode;

import client.Client;
import tool.FileUtil;
import tool.CountdownTimer;

import javax.swing.*;

public class ChineseToEnglishGame extends GameGui {

    public ChineseToEnglishGame(Client client) {
        super(GameMode.CHINESE_TO_ENGLISH, client);
        countdownTimer.setOnTimeOverCallback(() -> handleTimeOver()); // 设置回调函数
    }

    @Override
    protected void initializeButtonListeners() {
        submitButton.addActionListener(e -> checkAnswer());
        inputField.addActionListener(e -> checkAnswer());
    }

    private void checkAnswer() {
        String userInput = inputField.getText().trim();
        boolean isCorrect = userInput.equalsIgnoreCase(currentWord);

        if (isCorrect) {
            JOptionPane.showMessageDialog(j, "回答正确！");
            FileUtil.saveMasteredWord(currentWord, currentMeaning);
        } else {
            JOptionPane.showMessageDialog(j, "回答错误，正确答案是：" + currentWord);
            FileUtil.saveUnmasteredWord(currentWord, currentMeaning, "作答错误");
        }

        boolean hasSubmitted = !userInput.isEmpty();
        updateScoreAndHp(isCorrect, hasSubmitted);

        countdownTimer.stop(); // 停止计时器
        fetchRandomChineseExplanation(); // 获取新题目并重启计时器
    }

    @Override
    protected void initializeGameLogic() {
        fetchRandomChineseExplanation();
        countdownTimer.start(); // 启动计时器
    }

    private void handleTimeOver() {
        JOptionPane.showMessageDialog(j, "您没有回答，正确答案是：" + currentWord);
        FileUtil.saveUnmasteredWord(currentWord, currentMeaning, "未作答");
        updateScoreAndHp(false, false); // 处理未作答的情况
        countdownTimer.stop(); // 停止计时器
        fetchRandomChineseExplanation(); // 获取新题目并重启计时器
    }
}









//package client.gameMode;
//
//import tool.FileUtil;
//
//import javax.swing.*;
//
//public class ChineseToEnglishGame extends GameGui {
//
//    public ChineseToEnglishGame() {
//        super(GameMode.CHINESE_TO_ENGLISH);
//        countdownTimer.setOnTimeOverCallback(() -> handleTimeOver()); // 设置回调函数
//    }
//
//    @Override
//    protected void initializeButtonListeners() {
//        submitButton.addActionListener(e -> checkAnswer());
//        inputField.addActionListener(e -> checkAnswer());
//    }
//
//    private void checkAnswer() {
//        String userInput = inputField.getText().trim();
//        String correctMiddlePart = currentWord.substring(1, currentWord.length() - 1);
//        boolean isCorrect = userInput.equalsIgnoreCase(correctMiddlePart);
//
//        if (isCorrect) {
//            JOptionPane.showMessageDialog(j, "回答正确！");
//            FileUtil.saveMasteredWord(currentWord, currentMeaning);
//        } else {
//            JOptionPane.showMessageDialog(j, "回答错误，正确答案是：" + currentWord);
//            FileUtil.saveUnmasteredWord(currentWord, currentMeaning, "作答错误");
//        }
//
//        boolean hasSubmitted = !userInput.isEmpty();
//        updateScoreAndHp(isCorrect, hasSubmitted);
//
//        countdownTimer.stop(); // 停止计时器
//        fetchRandomChineseExplanation(); // 获取新题目并重启计时器
//    }
//
//
//    @Override
//    protected void initializeGameLogic() {
//        fetchRandomChineseExplanation();
//        countdownTimer.start(); // 启动计时器
//    }
//
//    private void handleTimeOver() {
//        JOptionPane.showMessageDialog(j, "您没有回答，正确答案是：" + currentWord);
//        FileUtil.saveUnmasteredWord(currentWord, currentMeaning, "未作答");
//        updateScoreAndHp(false, false); // 处理未作答的情况
//        countdownTimer.stop(); // 停止计时器
//        fetchRandomChineseExplanation(); // 获取新题目并重启计时器
//    }
//}





