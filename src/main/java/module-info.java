module com.velha {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.velha;
    opens com.velha to javafx.fxml, javafx.graphics;
}