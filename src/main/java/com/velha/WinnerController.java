package com.velha;

import com.velha.model.Game;
import com.velha.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WinnerController {

    @FXML
    private Label winnerLabel;

    @FXML
    public void initialize() {
        Game game = App.getGame();
        winnerLabel.setText(game.getWinnerMessage());
    }

    @FXML
    public void onPlayAgain() {
        App.createGame(App.getGame().getTimeLimit());
        SceneManager.loadScene("game");
    }

    @FXML
    public void onBackToMenu() {
        SceneManager.loadScene("menu");
    }
}