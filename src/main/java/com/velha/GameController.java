package com.velha;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private GridPane boardGrid;

    @FXML
    private Label statusLabel;

    @FXML
    private Label timerLabel;

    private Button[] cells = new Button[9];
    private int timeRemaining;
    private Thread timerThread;

    @FXML
    public void initialize() {
        buildBoard();
        startTimer();
        updateStatusLabel();
    }

    private void buildBoard() {
        for (int i = 0; i < 9; i++) {
            Button cell = new Button();
            cell.getStyleClass().add("cell");
            cell.setText("");
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            final int pos = i;
            cell.setOnAction(e -> onCellClick(pos));
            cells[i] = cell;
            boardGrid.add(cell, i % 3, i / 3);
        }
    }

    private void onCellClick(int position) {
        if (GameLogic.isGameOver()) return;
        if (!GameLogic.makeMove(position)) return;

        cells[position].setText(String.valueOf(GameLogic.getCurrentPlayer()));

        char winner = GameLogic.checkWinner();
        boolean draw = GameLogic.checkDraw();

        if (winner != '\0' || draw) {
            stopTimer();
            GameLogic.setGameOver(true);
            App.loadScene("winner");
            return;
        }

        GameLogic.switchPlayer();
        updateStatusLabel();
        restartTimer();
    }

    private void updateStatusLabel() {
        statusLabel.setText("Vez do jogador: " + GameLogic.getCurrentPlayer());
    }

    private void startTimer() {
        timeRemaining = GameLogic.getTimeLimit();
        updateTimerLabel();

        timerThread = new Thread(() -> {
            while (timeRemaining > 0 && !GameLogic.isGameOver()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                timeRemaining--;
                Platform.runLater(this::updateTimerLabel);
            }
            if (!GameLogic.isGameOver()) {
                Platform.runLater(this::onTimeUp);
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    private void restartTimer() {
        stopTimer();
        startTimer();
    }

    private void stopTimer() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
    }

    private void updateTimerLabel() {
        timerLabel.setText("Tempo: " + timeRemaining + "s");
        if (timeRemaining <= 3) {
            timerLabel.setStyle("-fx-text-fill: red; -fx-font-size: 18px;");
        } else {
            timerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        }
    }

    private void onTimeUp() {
        GameLogic.setGameOver(true);
        GameLogic.switchPlayer();
        GameLogic.setWinnerMessage("Jogador " + GameLogic.getCurrentPlayer() + " venceu! (Tempo esgotado)");
        App.loadScene("winner");
    }
}
