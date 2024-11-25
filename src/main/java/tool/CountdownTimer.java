package tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountdownTimer {
    private Timer timer;
    private int timeLeft; // 倒计时剩余时间（秒）
    private JLabel timerLabel;
    private Runnable onTimeOverCallback; // 时间到时的回调

    public CountdownTimer(int initialTime, JLabel timerLabel) {
        this.timeLeft = initialTime;
        this.timerLabel = timerLabel;
        initializeTimer();
    }

    private void initializeTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    updateTimerLabel();
                } else {
                    timer.stop();
                    System.out.println("时间到！");
                    if (onTimeOverCallback != null) {
                        onTimeOverCallback.run(); // 调用回调函数
                    }
                }
            }
        });
    }

    public void start() {
        timeLeft = 10; // 重置时间为10秒
        updateTimerLabel();
        timer.start();
    }

    private void updateTimerLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("时间: %02d:%02d", minutes, seconds));
    }

    public boolean isTimeOver() {
        return timeLeft == 0;
    }

    public void setOnTimeOverCallback(Runnable callback) {
        this.onTimeOverCallback = callback;
    }

    public void stop() {
        timer.stop();
    }
}





