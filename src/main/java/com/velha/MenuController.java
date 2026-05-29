package com.velha;

import com.velha.model.Game;
import com.velha.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class MenuController {

    private static final List<String> TIME_OPTIONS = List.of("5 segundos", "10 segundos", "15 segundos", "20 segundos");
    private static final List<Integer> TIME_VALUES = List.of(5, 10, 15, 20);

    @FXML
    private ChoiceBox<String> timeChoice;

    @FXML
    public void initialize() {
        timeChoice.getItems().addAll(TIME_OPTIONS);
        timeChoice.setValue("10 segundos");
    }

    @FXML
    public void onStartClick() {
        String selected = timeChoice.getValue();
        int seconds = parseTimeOption(selected);
        App.createGame(seconds);
        SceneManager.loadScene("game");
    }

    private int parseTimeOption(String option) {
        int index = TIME_OPTIONS.indexOf(option);
        return index >= 0 ? TIME_VALUES.get(index) : 10;
    }
}