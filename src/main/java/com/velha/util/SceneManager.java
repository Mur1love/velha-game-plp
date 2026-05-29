package com.velha.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneManager {

    private static Stage primaryStage;

    private SceneManager() {
        // utility class
    }

    public static void init(Stage stage) {
        primaryStage = stage;
    }

    public static void loadScene(String name) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                SceneManager.class.getResource("/" + name + ".fxml")));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(
                SceneManager.class.getResource("/style.css")).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}