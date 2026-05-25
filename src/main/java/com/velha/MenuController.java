package com.velha;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class MenuController {

    @FXML
    private ChoiceBox<String> timeChoice;

    @FXML
    public void initialize() {
        timeChoice.getItems().addAll("5 segundos", "10 segundos", "15 segundos", "20 segundos");
        timeChoice.setValue("10 segundos");
    }

    @FXML
    public void onStartClick() {
        String selected = timeChoice.getValue();
        int seconds = parseTimeOption(selected);
        GameLogic.setTimeLimit(seconds);
        GameLogic.resetBoard();
        App.loadScene("game");
    }

    private int parseTimeOption(String option) {
        List<String> options = List.of("5 segundos", "10 segundos", "15 segundos", "20 segundos");
        List<Integer> values = List.of(5, 10, 15, 20);
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(option)) {
                return values.get(i);
            }
        }
        return 10;
    }
}
