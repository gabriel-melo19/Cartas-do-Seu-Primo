package com.cardgame.ui;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Gerencia o empilhamento e troca de telas usando FXMLLoader.
 * Mantém o fundo persistente e aplica animações de zoom entre telas.
 * Equivale à antiga JanelaPrincipal do projeto anterior.
 */
public class ScreenManager extends StackPane {

    private static final int DURACAO_ZOOM_MS = 500;
    private static final int DURACAO_ZOOM_REVERSO_MS = 400;

    private final ImageView fundoPersistente;

    public ScreenManager() {
        Image imagemFundo = new Image(
                getClass().getResourceAsStream("/com/cardgame/img/fundos/fundo_menu.jpg")
        );

        fundoPersistente = new ImageView(imagemFundo);
        fundoPersistente.fitWidthProperty().bind(this.widthProperty());
        fundoPersistente.fitHeightProperty().bind(this.heightProperty());
        fundoPersistente.setPreserveRatio(false);

        getChildren().add(fundoPersistente);

        navegarPara("/com/cardgame/fxml/menu_principal.fxml");
    }

    /**
     * Carrega um FXML, injeta o ScreenManager no controller
     * e adiciona a tela no topo da pilha visual.
     */
    public void navegarPara(String caminhoFxml) {
        try {
            FXMLLoader carregador = new FXMLLoader(
                    getClass().getResource(caminhoFxml)
            );
            Node novaTela = carregador.load();

            Object controller = carregador.getController();
            if (controller instanceof ControladorDeFluxo controladorDeFluxo) {
                controladorDeFluxo.configurar(this);
            }

            removerTelaDoTopo();
            getChildren().add(novaTela);

        } catch (IOException e) {
            System.err.println("[ERRO] Falha ao carregar tela: " + caminhoFxml + " — " + e.getMessage());
        }
    }

    /**
     * Zoom de entrada no fundo e depois navega para a seleção de deck.
     */
    public void zoomEntradaENavegar(String caminhoFxml) {
        ScaleTransition zoom = new ScaleTransition(
                Duration.millis(DURACAO_ZOOM_MS), fundoPersistente
        );
        zoom.setToX(1.2);
        zoom.setToY(1.2);
        zoom.setOnFinished(e -> navegarPara(caminhoFxml));
        zoom.play();
    }

    /**
     * Zoom reverso no fundo e depois volta para o nickname.
     */
    public void zoomReversaENavegar(String caminhoFxml) {
        removerTelaDoTopo();

        ScaleTransition zoom = new ScaleTransition(
                Duration.millis(DURACAO_ZOOM_REVERSO_MS), fundoPersistente
        );
        zoom.setToX(1.0);
        zoom.setToY(1.0);
        zoom.setOnFinished(e -> navegarPara(caminhoFxml));
        zoom.play();
    }

    private void removerTelaDoTopo() {
        if (getChildren().size() > 1) {
            getChildren().remove(getChildren().size() - 1);
        }
    }
}