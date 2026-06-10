package com.cardgame.ui.controller;

import com.cardgame.logic.MontadorDeck;
import com.cardgame.model.Carta;
import com.cardgame.model.Deck;
import com.cardgame.persistence.RepositorioJSON;
import com.cardgame.ui.ControladorDeFluxo;
import com.cardgame.ui.ScreenManager;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.List;

public class SelecaoDeckController implements ControladorDeFluxo {

    private static final int DURACAO_HOVER_MS = 140;

    @FXML private Label tituloTela;
    @FXML private FlowPane painelListaDecks;
    @FXML private VBox painelDeckSelecionado;
    @FXML private FlowPane painelCartasDeck;
    @FXML private Button botaoConfirmarDeck;
    @FXML private Button botaoSelecionarOutroDeck;
    @FXML private Button botaoVoltar;

    private ScreenManager screenManager;
    private Deck deckSelecionadoAtual;

    @FXML
    public void initialize() {
        configurarBotao(botaoConfirmarDeck);
        configurarBotao(botaoSelecionarOutroDeck);
        configurarBotao(botaoVoltar);
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

        painelCartasDeck.getChildren().clear();

        for (Carta carta : deck.getCartas()) {
            VBox cartaVisual = criarCartaVisual(carta);
            painelCartasDeck.getChildren().add(cartaVisual);
        }
    }

    private VBox criarCartaVisual(Carta carta) {
        VBox cartaBox = new VBox(8);
        cartaBox.setAlignment(Pos.TOP_CENTER);
        cartaBox.getStyleClass().add("carta-deck-selecionado");
        cartaBox.setPrefWidth(170);
        cartaBox.setMinWidth(170);
        cartaBox.setMaxWidth(170);

        ImageView imagemCarta = criarImagemCarta(carta, 150, 180);

        Label nomeCarta = new Label(carta.getNome());
        nomeCarta.setWrapText(true);
        nomeCarta.setMaxWidth(145);
        nomeCarta.getStyleClass().add("nome-carta-selecionada");

        Label atributosCarta = new Label("ATK: " + carta.getPoderDeLuta() + "   HP: " + carta.getVida());
        atributosCarta.getStyleClass().add("atributos-carta-selecionada");

        cartaBox.getChildren().addAll(imagemCarta, nomeCarta, atributosCarta);

        aplicarHover(cartaBox);

        return cartaBox;
    }

    private ImageView criarImagemCarta(Carta carta, double largura, double altura) {
        String caminho = "/" + carta.getImagem();
        InputStream is = getClass().getResourceAsStream(caminho);

        ImageView imageView;

        if (is != null) {
            Image imagem = new Image(is);
            imageView = new ImageView(imagem);
        } else {
            System.out.println("[IMG] Não encontrou imagem: " + caminho);

            imageView = new ImageView();
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
            return;
        }

        System.out.println("[DECK] Deck confirmado: " + deckSelecionadoAtual.getNome());
    }

    @FXML
    public void selecionarOutroDeck() {
        deckSelecionadoAtual = null;

        tituloTela.setText("Escolha seu deck");
        painelCartasDeck.getChildren().clear();

        painelDeckSelecionado.setVisible(false);
        painelDeckSelecionado.setManaged(false);

        painelListaDecks.setVisible(true);
        painelListaDecks.setManaged(true);
    }

    @FXML
    public void voltarParaMenu() {
        screenManager.zoomReversaENavegar("/com/cardgame/fxml/nickname.fxml");
    }

    private void configurarBotao(Button botao) {
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