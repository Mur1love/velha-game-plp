package com.velha;

import com.velha.model.Game;
import com.velha.model.Symbol;
import com.velha.timer.GameTimer;
import com.velha.util.SceneManager;
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

    private final Button[] cells = new Button[9];
    private Game game;
    private GameTimer timer;

    @FXML
    public void initialize() {
        game = App.getGame();
        buildBoard();
        updateStatusLabel();
        startTimer();
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
        if (game.isGameOver()) return;

        Symbol previousSymbol = game.getCurrentPlayer().getSymbol();
        if (!game.makeMove(position)) return;

        cells[position].setText(previousSymbol.toString());

        if (game.isGameOver()) {
            stopTimer();
            SceneManager.loadScene("winner");
            return;
        }

        updateStatusLabel();
        timer.restart();
    }

    private void updateStatusLabel() {
        statusLabel.setText("Vez do jogador: " + game.getCurrentPlayer().getDisplayName());
    }

    private void startTimer() {
        timer = new GameTimer(
            game.getTimeLimit(),
            this::updateTimerLabel,
            this::onTimeUp
        );
        timer.start();
    }

    private void updateTimerLabel(int seconds) {
        timerLabel.setText("Tempo: " + seconds + "s");
        if (seconds <= 3) {
            timerLabel.setStyle("-fx-text-fill: red; -fx-font-size: 18px;");
        } else {
            timerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        }
    }

    private void onTimeUp() {
        game.forfeitDueToTimeout();
        SceneManager.loadScene("winner");
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}