package com.velha.timer;

import javafx.application.Platform;

import java.util.function.Consumer;

public class GameTimer {

    private int timeRemaining;
    private final int timeLimit;
    private Thread timerThread;
    private final Runnable onTimeUp;
    private final Consumer<Integer> onTick;

    public GameTimer(int timeLimit, Consumer<Integer> onTick, Runnable onTimeUp) {
        this.timeLimit = timeLimit;
        this.onTick = onTick;
        this.onTimeUp = onTimeUp;
    }

    public void start() {
        timeRemaining = timeLimit;
        onTick.accept(timeRemaining);

        timerThread = new Thread(() -> {
            while (timeRemaining > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                timeRemaining--;
                Platform.runLater(() -> onTick.accept(timeRemaining));
            }
            Platform.runLater(onTimeUp);
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public void restart() {
        stop();
        start();
    }

    public void stop() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }
}