package com.velha;

import javafx.application.Application;
import javafx.stage.Stage;
import com.velha.model.Game;
import com.velha.util.SceneManager;

public class App extends Application {

    private static Game game;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Jogo da Velha");
        primaryStage.setResizable(false);
        SceneManager.init(primaryStage);
        SceneManager.loadScene("menu");
    }

    public static Game getGame() {
        return game;
    }

    public static Game createGame(int timeLimit) {
        game = new Game(timeLimit);
        game.reset();
        return game;
    }

    public static void main(String[] args) {
        launch(args);
    }
}