package com.cardgame.ui.controller;

import com.cardgame.logic.SessaoJogo;
import com.cardgame.model.Bot;
import com.cardgame.persistence.RepositorioBots;
import com.cardgame.ui.ControladorDeFluxo;
import com.cardgame.ui.ScreenManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SelecaoAdversarioController implements ControladorDeFluxo {

    private static final int DURACAO_FADE_MS = 400;
    private static final String ESTILO_CARD = "card-adversario";
    private static final String ESTILO_CARD_SELECIONADO = "card-adversario-selecionado";

    @FXML private VBox containerPrincipal;
    @FXML private ScrollPane scrollAdversarios;
    @FXML private FlowPane painelListaAdversarios;

    @FXML private VBox painelAdversarioSelecionado;
    @FXML private StackPane containerImagemSelecionado;
    @FXML private ImageView imagemAdversarioSelecionado;
    @FXML private Label nomeAdversarioSelecionado;
    @FXML private Label dificuldadeAdversarioSelecionado;
    @FXML private Label descricaoAdversarioSelecionado;

    @FXML private Button botaoIniciarPartida;
    @FXML private Button botaoVoltar;

    private final List<Bot> botsDisponiveis = new ArrayList<>();
    private final List<VBox> cardsCriados = new ArrayList<>();

    private ScreenManager screenManager;
    private Bot botSelecionado;
    private VBox cardSelecionado;

    @FXML
    public void initialize() {
        if (containerPrincipal != null) {
            containerPrincipal.setOpacity(0.0);
        }

        ocultarPainelSelecionado();
        carregarBots();
        renderizarBots();

        if (!botsDisponiveis.isEmpty() && !cardsCriados.isEmpty()) {
            selecionarAdversario(botsDisponiveis.get(0), cardsCriados.get(0));
        }
    }

    @Override
    public void configurar(ScreenManager screenManager) {
        this.screenManager = screenManager;

        FadeTransition fade = new FadeTransition(Duration.millis(DURACAO_FADE_MS), containerPrincipal);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void carregarBots() {
        botsDisponiveis.clear();
        botsDisponiveis.addAll(RepositorioBots.listarBots());
    }

    private void renderizarBots() {
        painelListaAdversarios.getChildren().clear();
        cardsCriados.clear();

        for (Bot bot : botsDisponiveis) {
            VBox card = criarCardAdversario(bot);
            cardsCriados.add(card);
            painelListaAdversarios.getChildren().add(card);
        }
    }

    private VBox criarCardAdversario(Bot bot) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.getStyleClass().add(ESTILO_CARD);
        card.getProperties().put("bot", bot);

        ImageView imagem = criarImagemBot(bot, 170, 170);

        Label nome = new Label(bot.getNome());
        nome.getStyleClass().add("nome-adversario");
        nome.setWrapText(true);

        card.getChildren().addAll(imagem, nome);
        card.setOnMouseClicked(event -> selecionarAdversario(bot, card));

        return card;
    }

    private ImageView criarImagemBot(Bot bot, double largura, double altura) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(largura);
        imageView.setFitHeight(altura);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.getStyleClass().add("imagem-adversario");

        Image imagem = carregarImagemBot(bot);
        if (imagem != null) {
            imageView.setImage(imagem);
        } else {
            imageView.setOpacity(0.18);
        }

        return imageView;
    }

    private Image carregarImagemBot(Bot bot) {
        if (bot == null || bot.getImagem() == null || bot.getImagem().isBlank()) {
            return null;
        }

        String caminho = bot.getImagem().startsWith("/") ? bot.getImagem() : "/" + bot.getImagem();

        try (InputStream is = getClass().getResourceAsStream(caminho)) {
            return is != null ? new Image(is) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private void selecionarAdversario(Bot bot, VBox card) {
        botSelecionado = bot;
        atualizarCardSelecionado(card);
        atualizarPainelSelecionado(bot);
    }

    private void atualizarCardSelecionado(VBox novoCard) {
        if (cardSelecionado != null) {
            cardSelecionado.getStyleClass().remove(ESTILO_CARD_SELECIONADO);
        }

        cardSelecionado = novoCard;

        if (cardSelecionado != null && !cardSelecionado.getStyleClass().contains(ESTILO_CARD_SELECIONADO)) {
            cardSelecionado.getStyleClass().add(ESTILO_CARD_SELECIONADO);
        }
    }

    private void atualizarPainelSelecionado(Bot bot) {
        if (bot == null) {
            ocultarPainelSelecionado();
            return;
        }

        nomeAdversarioSelecionado.setText(bot.getNome());
        dificuldadeAdversarioSelecionado.setText(formatarDificuldade(bot.getDificuldade()));
        descricaoAdversarioSelecionado.setText(bot.getDescricao());

        Image imagem = carregarImagemBot(bot);
        imagemAdversarioSelecionado.setImage(imagem);
        imagemAdversarioSelecionado.setOpacity(imagem != null ? 1.0 : 0.18);

        painelAdversarioSelecionado.setManaged(true);
        painelAdversarioSelecionado.setVisible(true);
    }

    private void ocultarPainelSelecionado() {
        if (painelAdversarioSelecionado != null) {
            painelAdversarioSelecionado.setManaged(false);
            painelAdversarioSelecionado.setVisible(false);
        }
    }

    private String formatarDificuldade(Bot.Dificuldade dificuldade) {
        return switch (dificuldade) {
            case FACIL -> "Fácil";
            case MEDIO -> "Normal";
            case DIFICIL -> "Difícil";
        };
    }

    @FXML
    public void iniciarPartida() {
        if (botSelecionado == null) {
            System.out.println("[ADVERSARIO] Nenhum adversário foi selecionado.");
            return;
        }

        System.out.println("[ADVERSARIO] Partida iniciada contra: " + botSelecionado.getNome());
    }

    @FXML
    public void voltarParaSelecaoDeck() {
        if (screenManager == null) {
            return;
        }

        if (SessaoJogo.isCarregandoDeSave()) {
            screenManager.navegarPara("/com/cardgame/fxml/menu_carregar_save.fxml");
        } else {
            screenManager.navegarPara("/com/cardgame/fxml/selecao_deck.fxml");
        }
    }
}