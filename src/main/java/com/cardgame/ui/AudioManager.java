package com.cardgame.ui;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashSet;
import java.util.Set;

public final class AudioManager {

    private static final String CAMINHO_MUSICA_FUNDO = "/com/cardgame/audio/fundo_menu.wav";
    private static final String CAMINHO_SOM_HOVER = "/com/cardgame/audio/som_select.wav";

    private static MediaPlayer musicaFundoPlayer;
    private static AudioClip somHover;

    private AudioManager() {
    }

    public static void iniciarMusicaDeFundo() {
        try {
            if (musicaFundoPlayer != null) {
                if (musicaFundoPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                    musicaFundoPlayer.play();
                }
                return;
            }

            var recurso = AudioManager.class.getResource(CAMINHO_MUSICA_FUNDO);
            if (recurso == null) {
                System.out.println("[AUDIO] Música não encontrada: " + CAMINHO_MUSICA_FUNDO);
                return;
            }

            Media media = new Media(recurso.toExternalForm());
            musicaFundoPlayer = new MediaPlayer(media);
            musicaFundoPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicaFundoPlayer.setVolume(0.35);
            musicaFundoPlayer.play();

        } catch (Exception e) {
            System.out.println("[AUDIO] Erro ao iniciar música de fundo: " + e.getMessage());
        }
    }

    public static void tocarHover() {
        try {
            if (somHover == null) {
                var recurso = AudioManager.class.getResource(CAMINHO_SOM_HOVER);
                if (recurso == null) {
                    System.out.println("[AUDIO] Som hover não encontrado: " + CAMINHO_SOM_HOVER);
                    return;
                }

                somHover = new AudioClip(recurso.toExternalForm());
                somHover.setVolume(0.55);
            }

            somHover.play();

        } catch (Exception e) {
            System.out.println("[AUDIO] Erro ao tocar hover: " + e.getMessage());
        }
    }

    public static void aplicarSomInterativo(Parent raiz) {
        if (raiz == null) {
            return;
        }

        Set<Node> nodesInterativos = new HashSet<>();

        if (ehNodeInterativo(raiz)) {
            nodesInterativos.add(raiz);
        }

        for (Node node : raiz.lookupAll("*")) {
            if (ehNodeInterativo(node)) {
                nodesInterativos.add(node);
            }
        }

        for (Node node : nodesInterativos) {
            registrarHover(node);
        }
    }

    public static void aplicarSomInterativo(Node node) {
        if (node == null) {
            return;
        }

        registrarHover(node);
    }

    private static void registrarHover(Node node) {
        if (Boolean.TRUE.equals(node.getProperties().get("hover-audio-registrado"))) {
            return;
        }

        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> tocarHover());
        node.getProperties().put("hover-audio-registrado", true);
    }

    private static boolean ehNodeInterativo(Node node) {
        if (node == null || node.isDisable() || node.isMouseTransparent()) {
            return false;
        }

        if (node instanceof ButtonBase
                || node instanceof ComboBoxBase<?>
                || node instanceof ListView<?>
                || node instanceof TableView<?>
                || node instanceof TreeView<?>
                || node instanceof ListCell<?>
                || node instanceof TableRow<?>
                || node instanceof TreeCell<?>) {
            return true;
        }

        if (node.getOnMouseClicked() != null || node.getOnMousePressed() != null) {
            return true;
        }

        if (node.getCursor() == Cursor.HAND) {
            return true;
        }

        return node.isFocusTraversable();
    }
}