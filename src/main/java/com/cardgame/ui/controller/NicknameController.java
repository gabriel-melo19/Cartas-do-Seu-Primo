package com.cardgame.ui.controller;

import com.cardgame.ui.ControladorDeFluxo;
import com.cardgame.ui.ScreenManager;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller da tela de nickname.
 * Cuida da animação de entrada, label piscando,
 * validação do campo e navegação para a seleção de deck.
 */
public class NicknameController implements ControladorDeFluxo {

    private static final int DURACAO_FADE_MS = 500;
    private static final int DURACAO_HOVER_MS = 140;

    @FXML private VBox painelNickname;
    @FXML private TextField campoNickname;
    @FXML private Label labelDica;
    @FXML private Button botaoVoltar;

    private ScreenManager screenManager;

    @FXML
    public void initialize() {
        painelNickname.setOpacity(0.0);
        configurarBotao(botaoVoltar);
        iniciarAnimacaoPiscarDica();
    }

    @Override
    public void configurar(ScreenManager screenManager) {
        this.screenManager = screenManager;
        iniciarAnimacaoEntrada();
        Platform.runLater(() -> campoNickname.requestFocus());
    }

    private void iniciarAnimacaoEntrada() {
        FadeTransition fade = new FadeTransition(
                Duration.millis(DURACAO_FADE_MS), painelNickname
        );
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void iniciarAnimacaoPiscarDica() {
        FadeTransition piscar = new FadeTransition(
                Duration.millis(700), labelDica
        );
        piscar.setFromValue(1.0);
        piscar.setToValue(0.2);
        piscar.setAutoReverse(true);
        piscar.setCycleCount(Animation.INDEFINITE);
        piscar.play();
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
    public void confirmarNickname() {
        String nickname = campoNickname.getText().trim();

        if (nickname.isEmpty()) {
            System.out.println("[AVISO] Nickname não pode ser vazio.");
            return;
        }

        System.out.println("[NICKNAME] Confirmado: " + nickname);

        // Fade out no painel antes do zoom, igual ao projeto anterior
        FadeTransition fadeOut = new FadeTransition(
                Duration.millis(300), painelNickname
        );
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e ->
                screenManager.zoomEntradaENavegar("/com/cardgame/fxml/selecao_deck.fxml")
        );
        fadeOut.play();
    }

    @FXML
    public void voltarParaMenu() {
        screenManager.navegarPara("/com/cardgame/fxml/menu_principal.fxml");
    }
}