package com.cardgame;

import com.cardgame.ui.AudioManager;
import com.cardgame.ui.ScreenManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ScreenManager screenManager = new ScreenManager();

        Scene cenaPrincipal = new Scene(screenManager, 1280, 720);

        stage.setTitle("DeckHero");
        stage.setScene(cenaPrincipal);
        stage.setMaximized(true);
        stage.show();

        AudioManager.iniciarMusicaDeFundo();
        AudioManager.aplicarSomInterativo(screenManager);
    }

    public static void main(String[] args) {
        launch(args);
    }
}