package com.cardgame.ui.controller;

import com.cardgame.ui.ControladorDeFluxo;
import com.cardgame.ui.ScreenManager;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller do menu principal.
 * Cuida das animações de entrada e da navegação para o nickname.
 */
public class MenuPrincipalController implements ControladorDeFluxo {

    private static final int DURACAO_FADE_MS = 600;
    private static final int DURACAO_HOVER_MS = 140;

    @FXML private VBox painelMenu;
    @FXML private Button botaoNovoJogo;
    @FXML private Button botaoCarregarJogo;
    @FXML private Button botaoSair;

    private ScreenManager screenManager;

    @FXML
    public void initialize() {
        painelMenu.setOpacity(0.0);
        configurarBotao(botaoNovoJogo);
        configurarBotao(botaoCarregarJogo);
        configurarBotao(botaoSair);
    }

    @Override
    public void configurar(ScreenManager screenManager) {
        this.screenManager = screenManager;
        iniciarAnimacaoEntrada();
    }

    private void iniciarAnimacaoEntrada() {
        FadeTransition fade = new FadeTransition(
                Duration.millis(DURACAO_FADE_MS), painelMenu
        );
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void configurarBotao(Button botao) {
        botao.setCache(true);
        botao.setCacheHint(CacheHint.SPEED);

        ScaleTransition aumentar = new ScaleTransition(
                Duration.millis(DURACAO_HOVER_MS), botao
        );
        aumentar.setToX(1.08);
        aumentar.setToY(1.08);
        aumentar.setInterpolator(Interpolator.SPLINE(0.16, 1.0, 0.3, 1.0));

        ScaleTransition voltar = new ScaleTransition(
                Duration.millis(DURACAO_HOVER_MS), botao
        );
        voltar.setToX(1.0);
        voltar.setToY(1.0);
        voltar.setInterpolator(Interpolator.SPLINE(0.16, 1.0, 0.3, 1.0));

        botao.setOnMouseEntered(e -> {
            voltar.stop();
            aumentar.playFromStart();
        });

        botao.setOnMouseExited(e -> {
            aumentar.stop();
            voltar.playFromStart();
        });
    }

    @FXML
    public void iniciarNovoJogo() {
        screenManager.navegarPara("/com/cardgame/fxml/nickname.fxml");
    }

    @FXML
    public void carregarJogo() {
        System.out.println("[MENU] Acessando sistema de persistência de saves...");
    }

    @FXML
    public void sairDoJogo() {
        System.out.println("[MENU] Aplicação encerrada com sucesso pelo jogador.");
        Platform.exit();
    }
}