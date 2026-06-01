module com.cardgame.cardgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.cardgame.ui.controller to javafx.fxml;
    opens com.cardgame.cardgame to javafx.fxml;
    exports com.cardgame.cardgame;
    exports com.cardgame.ui.controller;
}