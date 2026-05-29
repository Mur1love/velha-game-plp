module com.velha {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.velha;
    exports com.velha.model;
    exports com.velha.timer;
    exports com.velha.util;

    opens com.velha to javafx.fxml, javafx.graphics;
    opens com.velha.model to javafx.fxml;
    opens com.velha.util to javafx.fxml;
}