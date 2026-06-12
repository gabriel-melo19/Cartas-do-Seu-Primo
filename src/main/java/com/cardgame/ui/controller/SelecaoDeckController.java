package com.cardgame.ui.controller;

import com.cardgame.logic.MontadorDeck;
import com.cardgame.logic.SaveData;
import com.cardgame.logic.SessaoJogo;
import com.cardgame.model.Carta;
import com.cardgame.model.Deck;
import com.cardgame.persistence.RepositorioJSON;
import com.cardgame.persistence.RepositorioSave;
import com.cardgame.ui.ControladorDeFluxo;
import com.cardgame.ui.ScreenManager;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.List;

public class SelecaoDeckController implements ControladorDeFluxo {

    private static final int DURACAO_HOVER_MS = 140;

    @FXML private Label tituloTela;
    @FXML private FlowPane painelListaDecks;
    @FXML private VBox painelDeckSelecionado;
    @FXML private HBox painelCartasDeck;
    @FXML private Button botaoConfirmarDeck;
    @FXML private Button botaoSelecionarOutroDeck;
    @FXML private Button botaoVoltar;

    @FXML private StackPane overlayDetalhesCarta;
    @FXML private VBox janelaDetalhesCarta;
    @FXML private Button botaoFecharDetalhes;
    @FXML private Label nomeDetalhesCarta;
    @FXML private Label atkDetalhesCarta;
    @FXML private Label hpDetalhesCarta;
    @FXML private Label descricaoDetalhesCarta;

    private ScreenManager screenManager;
    private Deck deckSelecionadoAtual;

    @FXML
    public void initialize() {
        configurarBotao(botaoConfirmarDeck);
        configurarBotao(botaoSelecionarOutroDeck);
        configurarBotao(botaoVoltar);
        configurarBotao(botaoFecharDetalhes);

        if (overlayDetalhesCarta != null) {
            overlayDetalhesCarta.setVisible(false);
            overlayDetalhesCarta.setManaged(false);
            overlayDetalhesCarta.setOnMouseClicked(event -> fecharDetalhesCarta());
        }

        if (janelaDetalhesCarta != null) {
            janelaDetalhesCarta.setOnMouseClicked(event -> event.consume());
        }
    }

    @Override
    public void configurar(ScreenManager screenManager) {
        this.screenManager = screenManager;
        carregarDecksNaTela();
    }

    private void carregarDecksNaTela() {
        painelListaDecks.getChildren().clear();

        List<Carta> cartas = RepositorioJSON.listarCartas();
        MontadorDeck montadorDeDecks = new MontadorDeck(cartas);
        List<Deck> decks = montadorDeDecks.montarDecksPadrao();

        if (decks.isEmpty()) {
            tituloTela.setText("Nenhum deck disponível");
            return;
        }

        tituloTela.setText("Escolha seu deck");

        for (Deck deck : decks) {
            VBox cardDeck = criarCardDeck(deck);
            painelListaDecks.getChildren().add(cardDeck);
        }
    }

    private VBox criarCardDeck(Deck deck) {
        VBox cardDeck = new VBox(14);
        cardDeck.setAlignment(Pos.CENTER);
        cardDeck.getStyleClass().add("card-deck");
        cardDeck.setPrefWidth(260);
        cardDeck.setMaxWidth(260);
        cardDeck.setMinWidth(260);

        Label nomeDeck = new Label(deck.getNome());
        nomeDeck.getStyleClass().add("titulo-deck");

        HBox miniPreview = new HBox(-18);
        miniPreview.setAlignment(Pos.CENTER);
        miniPreview.getStyleClass().add("mini-preview-cartas");

        List<Carta> cartasDestaque = deck.getPrimeirasCartas(2);
        for (Carta carta : cartasDestaque) {
            VBox miniCarta = criarMiniCartaDeck(carta);
            miniPreview.getChildren().add(miniCarta);
        }

        Label descricaoDeck = new Label(deck.getDescricao());
        descricaoDeck.setWrapText(true);
        descricaoDeck.setAlignment(Pos.CENTER);
        descricaoDeck.setMaxWidth(220);
        descricaoDeck.getStyleClass().add("desc-deck");

        cardDeck.getChildren().addAll(nomeDeck, miniPreview, descricaoDeck);

        aplicarHover(cardDeck);
        cardDeck.setOnMouseClicked(event -> mostrarDeckSelecionado(deck));

        return cardDeck;
    }

    private VBox criarMiniCartaDeck(Carta carta) {
        VBox miniCarta = new VBox(6);
        miniCarta.setAlignment(Pos.CENTER);
        miniCarta.getStyleClass().add("mini-carta-preview");
        miniCarta.setRotate(Math.random() > 0.5 ? -8 : 8);

        ImageView imagemMiniCarta = criarImagemCarta(carta, 72, 96);

        Label nomeCarta = new Label(carta.getNome());
        nomeCarta.setWrapText(true);
        nomeCarta.setMaxWidth(82);
        nomeCarta.getStyleClass().add("mini-carta-nome");

        miniCarta.getChildren().addAll(imagemMiniCarta, nomeCarta);
        return miniCarta;
    }

    private void mostrarDeckSelecionado(Deck deck) {
        this.deckSelecionadoAtual = deck;

        tituloTela.setText(deck.getNome());
        painelListaDecks.setVisible(false);
        painelListaDecks.setManaged(false);

        painelDeckSelecionado.setVisible(true);
        painelDeckSelecionado.setManaged(true);

        botaoVoltar.setVisible(false);
        botaoVoltar.setManaged(false);

        painelCartasDeck.getChildren().clear();

        for (Carta carta : deck.getCartas()) {
            VBox cartaVisual = criarCartaVisual(carta);
            painelCartasDeck.getChildren().add(cartaVisual);
        }
    }

    private VBox criarCartaVisual(Carta carta) {
        VBox cartaBox = new VBox();
        cartaBox.setAlignment(Pos.CENTER);
        cartaBox.getStyleClass().add("carta-deck-selecionado");
        cartaBox.setPrefWidth(212);
        cartaBox.setMinWidth(212);
        cartaBox.setMaxWidth(212);

        StackPane cartaStack = new StackPane();
        cartaStack.setAlignment(Pos.CENTER);
        cartaStack.getStyleClass().add("carta-stack");
        cartaStack.setPrefWidth(212);
        cartaStack.setMinWidth(212);
        cartaStack.setMaxWidth(212);
        cartaStack.setPrefHeight(312);
        cartaStack.setMinHeight(312);
        cartaStack.setMaxHeight(312);

        ImageView imagemCarta = criarImagemCarta(carta, 212, 312);
        imagemCarta.getStyleClass().add("imagem-carta-principal");

        Label nomeCarta = new Label(carta.getNome());
        nomeCarta.setWrapText(true);
        nomeCarta.setMaxWidth(180);
        nomeCarta.setAlignment(Pos.CENTER);
        nomeCarta.getStyleClass().add("nome-carta-selecionada");
        StackPane.setAlignment(nomeCarta, Pos.BOTTOM_CENTER);
        StackPane.setMargin(nomeCarta, new Insets(0, 16, 14, 16));

        Label atkCarta = new Label("ATK: " + carta.getPoderDeLutaBase());
        atkCarta.getStyleClass().add("atk-carta-selecionada");
        StackPane.setAlignment(atkCarta, Pos.BOTTOM_LEFT);
        StackPane.setMargin(atkCarta, new Insets(0, 10, 54, 10));

        Label hpCarta = new Label("HP: " + carta.getVidaBase());
        hpCarta.getStyleClass().add("hp-carta-selecionada");
        StackPane.setAlignment(hpCarta, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(hpCarta, new Insets(0, 10, 54, 10));

        cartaStack.getChildren().addAll(imagemCarta, nomeCarta, atkCarta, hpCarta);
        cartaBox.getChildren().add(cartaStack);

        aplicarHover(cartaBox);
        cartaBox.setOnMouseClicked(event -> mostrarDetalhesCarta(carta));

        return cartaBox;
    }

    private void mostrarDetalhesCarta(Carta carta) {
        nomeDetalhesCarta.setText(carta.getNome());
        atkDetalhesCarta.setText("ATK: " + carta.getPoderDeLutaBase());
        hpDetalhesCarta.setText("HP: " + carta.getVidaBase());

        String descricao = carta.getDescricao();
        if (descricao == null || descricao.isBlank()) {
            descricaoDetalhesCarta.setText("Esta carta não possui descrição cadastrada.");
        } else {
            descricaoDetalhesCarta.setText(descricao);
        }

        overlayDetalhesCarta.setVisible(true);
        overlayDetalhesCarta.setManaged(true);
        overlayDetalhesCarta.toFront();
    }

    @FXML
    public void fecharDetalhesCarta() {
        overlayDetalhesCarta.setVisible(false);
        overlayDetalhesCarta.setManaged(false);
    }

    private Image carregarImagem(Carta carta) {
        String caminho = "/" + carta.getImagem();
        InputStream is = getClass().getResourceAsStream(caminho);

        if (is != null) {
            return new Image(is);
        }

        System.out.println("[IMG] Não encontrou imagem: " + caminho);
        return null;
    }

    private ImageView criarImagemCarta(Carta carta, double largura, double altura) {
        Image imagem = carregarImagem(carta);
        ImageView imageView = new ImageView();

        if (imagem != null) {
            imageView.setImage(imagem);
        } else {
            imageView.setStyle("-fx-background-color: rgba(255,255,255,0.10);");
        }

        imageView.setFitWidth(largura);
        imageView.setFitHeight(altura);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        return imageView;
    }

    @FXML
    public void confirmarDeckSelecionado() {
        if (deckSelecionadoAtual == null) {
            System.out.println("[DECK] Nenhum deck foi selecionado.");
            return;
        }

        String nickname = obterNicknameAtual();
        SaveData saveExistente = RepositorioSave.carregar(nickname);

        if (saveExistente == null) {
            saveExistente = new SaveData(nickname, deckSelecionadoAtual.getId());
        } else {
            saveExistente.setDeckIdSelecionado(deckSelecionadoAtual.getId());
        }

        RepositorioSave.salvar(saveExistente);

        System.out.println("[DECK] Deck confirmado: " + deckSelecionadoAtual.getNome());
        System.out.println("[SAVE] Save atualizado para: " + nickname);

        if (screenManager != null) {
            screenManager.navegarPara("/com/cardgame/fxml/selecao_adversario.fxml");
        }
    }

    private String obterNicknameAtual() {
        String nickname = SessaoJogo.getNicknameAtual();
        return (nickname == null || nickname.isBlank()) ? "player" : nickname;
    }

    @FXML
    public void selecionarOutroDeck() {
        deckSelecionadoAtual = null;

        tituloTela.setText("Escolha seu deck");
        painelCartasDeck.getChildren().clear();

        fecharDetalhesCarta();

        painelDeckSelecionado.setVisible(false);
        painelDeckSelecionado.setManaged(false);

        painelListaDecks.setVisible(true);
        painelListaDecks.setManaged(true);

        botaoVoltar.setVisible(true);
        botaoVoltar.setManaged(true);
    }

    @FXML
    public void voltarParaMenu() {
        screenManager.zoomReversaENavegar("/com/cardgame/fxml/nickname.fxml");
    }

    private void configurarBotao(Button botao) {
        if (botao == null) {
            return;
        }

        botao.setCache(true);
        botao.setCacheHint(CacheHint.SPEED);
        aplicarHover(botao);
    }

    private void aplicarHover(Node node) {
        ScaleTransition aumentar = new ScaleTransition(Duration.millis(DURACAO_HOVER_MS), node);
        aumentar.setToX(1.05);
        aumentar.setToY(1.05);
        aumentar.setInterpolator(Interpolator.SPLINE(0.16, 1.0, 0.3, 1.0));

        ScaleTransition voltar = new ScaleTransition(Duration.millis(DURACAO_HOVER_MS), node);
        voltar.setToX(1.0);
        voltar.setToY(1.0);
        voltar.setInterpolator(Interpolator.SPLINE(0.16, 1.0, 0.3, 1.0));

        node.setOnMouseEntered(e -> {
            voltar.stop();
            aumentar.playFromStart();
        });

        node.setOnMouseExited(e -> {
            aumentar.stop();
            voltar.playFromStart();
        });
    }
}