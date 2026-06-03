module com.cardgame.cardgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.cardgame.ui.controller to javafx.fxml;
    opens com.cardgame to javafx.fxml;
    exports com.cardgame;
    exports com.cardgame.ui.controller;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens com.cardgame.model to com.fasterxml.jackson.databind;
    opens com.cardgame.persistence to com.fasterxml.jackson.databind;
}