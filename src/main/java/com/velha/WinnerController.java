package com.velha;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WinnerController {

    @FXML
    private Label winnerLabel;

    @FXML
    public void initialize() {
        winnerLabel.setText(GameLogic.getWinnerMessage());
    }

    @FXML
    public void onPlayAgain() {
        GameLogic.resetBoard();
        App.loadScene("game");
    }

    @FXML
    public void onBackToMenu() {
        App.loadScene("menu");
    }
}