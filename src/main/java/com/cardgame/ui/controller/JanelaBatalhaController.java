package com.cardgame.ui.controller;

import com.cardgame.logic.DueloResultado;
import com.cardgame.logic.PreparadorBatalha;
import com.cardgame.logic.SaveData;
import com.cardgame.logic.SessaoJogo;
import com.cardgame.logic.SistemaCombate;
import com.cardgame.model.Bot;
import com.cardgame.model.Carta;
import com.cardgame.model.Deck;
import com.cardgame.model.JogadaResultado;
import com.cardgame.model.JogadorHumano;
import com.cardgame.ui.ControladorDeFluxo;
import com.cardgame.ui.ScreenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class JanelaBatalhaController implements ControladorDeFluxo {

    @FXML private Label labelNomeJogador;
    @FXML private Label labelNomeBot;
    @FXML private Label labelStatusTurno;
    @FXML private Label labelVidaJogador;
    @FXML private Label labelVidaBot;
    @FXML private Label labelVidaJogadorArena;
    @FXML private Label labelVidaBotArena;
    @FXML private Label labelCartaJogadorNome;
    @FXML private Label labelCartaJogadorStatus;
    @FXML private Label labelCartaBotNome;
    @FXML private Label labelCartaBotStatus;

    @FXML private StackPane campoJogador;
    @FXML private StackPane campoBot;
    @FXML private HBox containerCartasJogador;

    @FXML private Button botaoPassarTurno;
    @FXML private Button botaoRender;

    @FXML private ImageView imageBotPerfil;

    private Bot botAtual;
    private Deck deckJogadorAtual;
    private JogadorHumano jogadorHumanoAtual;
    private SistemaCombate sistemaCombate;
    private ScreenManager screenManager;

    @Override
    public void configurar(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @FXML
    private void initialize() {
        carregarDadosIniciais();
        inicializarParticipantes();
        atualizarTelaInicial();
        renderizarMaoJogador();
        renderizarCampoCompleto();
        carregarImagemBot();
    }

    private void carregarDadosIniciais() {
        carregarNomeJogador();
        carregarDeckDoJogador();
        carregarBotAtual();
        carregarNomeBot();
    }

    private void inicializarParticipantes() {
        if (deckJogadorAtual == null) {
            return;
        }

        jogadorHumanoAtual = new JogadorHumano(
                "jogador-humano",
                labelNomeJogador.getText(),
                deckJogadorAtual,
                ""
        );

        if (botAtual != null) {
            sistemaCombate = new SistemaCombate(jogadorHumanoAtual, botAtual);
            sistemaCombate.inicioDoTurnoHumano();
        }
    }

    private void carregarNomeJogador() {
        String nickname = SessaoJogo.getNicknameAtual();

        if (nickname == null || nickname.isBlank()) {
            labelNomeJogador.setText("Jogador");
            return;
        }

        labelNomeJogador.setText(nickname);
    }

    private void carregarDeckDoJogador() {
        String nickname = SessaoJogo.getNicknameAtual();
        deckJogadorAtual = PreparadorBatalha.carregarDeckDoJogador(nickname);
    }

    private void carregarBotAtual() {
        botAtual = PreparadorBatalha.carregarBotDaPartida(SessaoJogo.getAdversarioAtual());
    }

    private void carregarNomeBot() {
        if (botAtual == null || botAtual.getNome() == null || botAtual.getNome().isBlank()) {
            labelNomeBot.setText("Adversário");
            return;
        }

        labelNomeBot.setText(botAtual.getNome());
    }

    private void atualizarTelaInicial() {
        atualizarContadores();

        labelStatusTurno.setText("Escolha uma carta da sua mão.");

        labelCartaJogadorNome.setText("Nenhuma carta");
        labelCartaJogadorStatus.setText(deckJogadorAtual != null
                ? "Escolha uma carta"
                : "Deck não encontrado");

        labelCartaBotNome.setText("Nenhuma carta");
        labelCartaBotStatus.setText(botAtual != null
                ? "Aguardando..."
                : "Bot não encontrado");
    }

    private void atualizarContadores() {
        int quantidadeJogador = jogadorHumanoAtual != null ? jogadorHumanoAtual.getQuantidadeNaMao() : 0;
        int quantidadeBot = botAtual != null ? botAtual.getQuantidadeNaMao() : 0;

        labelVidaJogador.setText(String.valueOf(quantidadeJogador));
        labelVidaBot.setText(String.valueOf(quantidadeBot));

        if (labelVidaJogadorArena != null) {
            labelVidaJogadorArena.setText(String.valueOf(quantidadeJogador));
        }

        if (labelVidaBotArena != null) {
            labelVidaBotArena.setText(String.valueOf(quantidadeBot));
        }
    }

    private void renderizarMaoJogador() {
        containerCartasJogador.getChildren().clear();

        List<Carta> cartas = jogadorHumanoAtual != null ? jogadorHumanoAtual.getMao() : null;
        if (cartas == null || cartas.isEmpty()) {
            Label aviso = new Label("Nenhuma carta na mão");
            aviso.getStyleClass().add("status-carta-campo");
            containerCartasJogador.getChildren().add(aviso);
            return;
        }

        for (Carta carta : cartas) {
            VBox cartaVisual = criarCartaMaoVisual(carta);
            containerCartasJogador.getChildren().add(cartaVisual);
        }
    }

    private VBox criarCartaMaoVisual(Carta carta) {
        VBox cartaBox = new VBox();
        cartaBox.setAlignment(Pos.CENTER);
        cartaBox.getStyleClass().add("carta-deck-selecionado");
        cartaBox.setPrefWidth(160);
        cartaBox.setMinWidth(160);
        cartaBox.setMaxWidth(160);

        StackPane cartaStack = new StackPane();
        cartaStack.setAlignment(Pos.CENTER);
        cartaStack.getStyleClass().add("carta-stack");
        cartaStack.setPrefWidth(160);
        cartaStack.setMinWidth(160);
        cartaStack.setMaxWidth(160);
        cartaStack.setPrefHeight(236);
        cartaStack.setMinHeight(236);
        cartaStack.setMaxHeight(236);

        ImageView imagemCarta = criarImagemCartaView(carta, 160, 236);
        imagemCarta.getStyleClass().add("imagem-carta-principal");

        Label nomeCarta = new Label(carta.getNome());
        nomeCarta.setWrapText(true);
        nomeCarta.setMaxWidth(126);
        nomeCarta.setAlignment(Pos.CENTER);
        nomeCarta.getStyleClass().add("nome-carta-selecionada");
        StackPane.setAlignment(nomeCarta, Pos.BOTTOM_CENTER);
        StackPane.setMargin(nomeCarta, new Insets(0, 10, 10, 10));

        Label atkCarta = new Label("ATK: " + carta.getPoderDeLutaAtual());
        atkCarta.getStyleClass().add("atk-carta-selecionada");
        StackPane.setAlignment(atkCarta, Pos.BOTTOM_LEFT);
        StackPane.setMargin(atkCarta, new Insets(0, 8, 44, 8));

        Label hpCarta = new Label("HP: " + carta.getVidaAtual());
        hpCarta.getStyleClass().add("hp-carta-selecionada");
        StackPane.setAlignment(hpCarta, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(hpCarta, new Insets(0, 8, 44, 8));

        Label elementoCarta = new Label(obterSeloElemento(carta));
        elementoCarta.getStyleClass().add("elemento-carta-selecionada");
        StackPane.setAlignment(elementoCarta, Pos.BOTTOM_CENTER);
        StackPane.setMargin(elementoCarta, new Insets(0, 0, 44, 0));

        cartaStack.getChildren().addAll(imagemCarta, nomeCarta, atkCarta, hpCarta, elementoCarta);
        cartaBox.getChildren().add(cartaStack);

        cartaBox.setOnMouseClicked(event -> selecionarCarta(carta));
        return cartaBox;
    }

    private StackPane criarCartaCampoVisual(Carta carta) {
        StackPane cartaStack = new StackPane();
        cartaStack.setAlignment(Pos.CENTER);
        cartaStack.getStyleClass().add("carta-stack");
        cartaStack.setPrefWidth(212);
        cartaStack.setMinWidth(212);
        cartaStack.setMaxWidth(212);
        cartaStack.setPrefHeight(312);
        cartaStack.setMinHeight(312);
        cartaStack.setMaxHeight(312);

        ImageView imagemCarta = criarImagemCartaView(carta, 212, 312);
        imagemCarta.getStyleClass().add("imagem-carta-principal");

        Label nomeCarta = new Label(carta.getNome());
        nomeCarta.setWrapText(true);
        nomeCarta.setMaxWidth(180);
        nomeCarta.setAlignment(Pos.CENTER);
        nomeCarta.getStyleClass().add("nome-carta-selecionada");
        StackPane.setAlignment(nomeCarta, Pos.BOTTOM_CENTER);
        StackPane.setMargin(nomeCarta, new Insets(0, 16, 14, 16));

        Label atkCarta = new Label("ATK: " + carta.getPoderDeLutaAtual());
        atkCarta.getStyleClass().add("atk-carta-selecionada");
        StackPane.setAlignment(atkCarta, Pos.BOTTOM_LEFT);
        StackPane.setMargin(atkCarta, new Insets(0, 10, 54, 10));

        Label hpCarta = new Label("HP: " + carta.getVidaAtual());
        hpCarta.getStyleClass().add("hp-carta-selecionada");
        StackPane.setAlignment(hpCarta, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(hpCarta, new Insets(0, 10, 54, 10));

        Label elementoCarta = new Label(obterSeloElemento(carta));
        elementoCarta.getStyleClass().add("elemento-carta-selecionada");
        StackPane.setAlignment(elementoCarta, Pos.BOTTOM_CENTER);
        StackPane.setMargin(elementoCarta, new Insets(0, 0, 54, 0));

        cartaStack.getChildren().addAll(imagemCarta, nomeCarta, atkCarta, hpCarta, elementoCarta);
        return cartaStack;
    }

    private void renderizarCampoCompleto() {
        renderizarCampoJogador();
        renderizarCampoBot();
    }

    private void renderizarCampoJogador() {
        campoJogador.getChildren().clear();

        Carta carta = jogadorHumanoAtual != null ? jogadorHumanoAtual.getCartaNohTabuleiro() : null;
        if (carta == null) {
            VBox boxJogador = new VBox(8);
            boxJogador.setAlignment(Pos.CENTER);

            Label placeholderJogadorNome = new Label("Nenhuma carta");
            placeholderJogadorNome.getStyleClass().add("nome-carta-campo");

            Label placeholderJogadorStatus = new Label(
                    jogadorHumanoAtual != null ? "Escolha uma carta" : "Deck não encontrado"
            );
            placeholderJogadorStatus.getStyleClass().add("status-carta-campo");

            boxJogador.getChildren().addAll(placeholderJogadorNome, placeholderJogadorStatus);
            campoJogador.getChildren().add(boxJogador);

            labelCartaJogadorNome.setText("Nenhuma carta");
            labelCartaJogadorStatus.setText(jogadorHumanoAtual != null ? "Escolha uma carta" : "Deck não encontrado");
            return;
        }

        campoJogador.getChildren().add(criarCartaCampoVisual(carta));
        labelCartaJogadorNome.setText(carta.getNome());
        labelCartaJogadorStatus.setText("ATK " + carta.getPoderDeLutaAtual() + " | HP " + carta.getVidaAtual());
    }

    private void renderizarCampoBot() {
        campoBot.getChildren().clear();

        Carta carta = botAtual != null ? botAtual.getCartaNohTabuleiro() : null;
        if (carta == null) {
            VBox boxBot = new VBox(8);
            boxBot.setAlignment(Pos.CENTER);

            Label placeholderBotNome = new Label("Nenhuma carta");
            placeholderBotNome.getStyleClass().add("nome-carta-campo");

            Label placeholderBotStatus = new Label(botAtual != null ? "Aguardando..." : "Bot não encontrado");
            placeholderBotStatus.getStyleClass().add("status-carta-campo");

            boxBot.getChildren().addAll(placeholderBotNome, placeholderBotStatus);
            campoBot.getChildren().add(boxBot);

            labelCartaBotNome.setText("Nenhuma carta");
            labelCartaBotStatus.setText(botAtual != null ? "Aguardando..." : "Bot não encontrado");
            return;
        }

        campoBot.getChildren().add(criarCartaCampoVisual(carta));
        labelCartaBotNome.setText(carta.getNome());
        labelCartaBotStatus.setText("ATK " + carta.getPoderDeLutaAtual() + " | HP " + carta.getVidaAtual());
    }

    private void selecionarCarta(Carta carta) {
        if (jogadorHumanoAtual == null || carta == null) {
            labelStatusTurno.setText("Jogador não inicializado.");
            return;
        }

        List<Carta> maoAtual = jogadorHumanoAtual.getMao();
        int indiceSelecionado = encontrarIndiceCarta(maoAtual, carta);

        if (indiceSelecionado < 0) {
            labelStatusTurno.setText("Carta não encontrada na mão.");
            return;
        }

        JogadaResultado resultado = jogadorHumanoAtual.jogarNoTabuleiro(indiceSelecionado);

        if (resultado == JogadaResultado.ERRO_INDICE_INVALIDO) {
            labelStatusTurno.setText("Jogada inválida.");
            return;
        }

        if (resultado == JogadaResultado.TROCA_JA_USADA) {
            labelStatusTurno.setText("Você já trocou de carta neste turno.");
            return;
        }

        renderizarMaoJogador();
        renderizarCampoJogador();
        atualizarContadores();

        if (resultado == JogadaResultado.TROCA_REALIZADA) {
            labelStatusTurno.setText("Carta trocada. Passe o turno.");
        } else {
            labelStatusTurno.setText("Carta selecionada. Passe o turno.");
        }
    }

    private int encontrarIndiceCarta(List<Carta> cartas, Carta cartaAlvo) {
        for (int i = 0; i < cartas.size(); i++) {
            Carta atual = cartas.get(i);

            if (atual == cartaAlvo) {
                return i;
            }

            if (atual != null && atual.equals(cartaAlvo)) {
                return i;
            }
        }
        return -1;
    }

    @FXML
    private void passarTurno() {
        if (jogadorHumanoAtual == null || botAtual == null || sistemaCombate == null) {
            labelStatusTurno.setText("Batalha não inicializada.");
            return;
        }

        if (!jogadorHumanoAtual.temCartaNoTabuleiro()) {
            labelStatusTurno.setText("Selecione uma carta antes de passar o turno.");
            return;
        }

        DueloResultado resultado = sistemaCombate.passarTurno();

        renderizarMaoJogador();
        renderizarCampoCompleto();
        atualizarContadores();

        if (jogadorHumanoAtual.perdeuTudo() || botAtual.perdeuTudo()) {
            labelStatusTurno.setText(resultado.mensagem);
            encerrarBatalha(resultado);
            return;
        }

        labelStatusTurno.setText(resultado.mensagem != null && !resultado.mensagem.isBlank()
                ? resultado.mensagem
                : "Combate concluído. Próximo turno.");

        sistemaCombate.inicioDoTurnoHumano();
    }

    private void encerrarBatalha(DueloResultado resultado) {
        botaoPassarTurno.setDisable(true);
        botaoRender.setDisable(true);
        containerCartasJogador.setDisable(true);

        registrarResultadoDaPartida(resultado);

        Alert.AlertType tipo = resultado != null && resultado.vitoria
                ? Alert.AlertType.INFORMATION
                : Alert.AlertType.WARNING;

        Alert alert = new Alert(tipo);
        alert.setTitle("Fim de partida");
        alert.setHeaderText(resultado != null && resultado.vitoria ? "Você venceu!" : "Partida encerrada");
        alert.setContentText(resultado != null && resultado.mensagem != null
                ? resultado.mensagem
                : "A batalha terminou.");

        alert.showAndWait();

        voltarParaSelecaoAdversario();
    }

    private void registrarResultadoDaPartida(DueloResultado resultado) {
        if (resultado == null) {
            return;
        }

        String nickname = SessaoJogo.getNicknameAtual();
        if (nickname == null || nickname.isBlank()) {
            return;
        }

        try {
            File arquivoSave = new File("saves/" + nickname + ".json");
            ObjectMapper mapper = new ObjectMapper();

            SaveData saveData;
            if (arquivoSave.exists()) {
                saveData = mapper.readValue(arquivoSave, SaveData.class);
            } else {
                saveData = new SaveData();
                saveData.setNickname(nickname);
            }

            if (botAtual != null) {
                saveData.setUltimoBotEnfrentado(botAtual.getId());

                if (resultado.vitoria) {
                    saveData.marcarBotComoDerrotado(botAtual.getId());
                }
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(arquivoSave, saveData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void voltarParaSelecaoAdversario() {
        if (screenManager == null) {
            labelStatusTurno.setText("ScreenManager não configurado.");
            return;
        }

        screenManager.navegarPara("/com/cardgame/fxml/selecao_adversario.fxml");
    }

    private void carregarImagemBot() {
        if (imageBotPerfil == null || botAtual == null) {
            return;
        }

        String caminhoImagem = botAtual.getImagem();

        if (caminhoImagem == null || caminhoImagem.isBlank()) {
            imageBotPerfil.setVisible(false);
            imageBotPerfil.setManaged(false);
            return;
        }

        InputStream is = getClass().getResourceAsStream("/" + caminhoImagem);
        if (is != null) {
            imageBotPerfil.setImage(new Image(is));
        } else {
            imageBotPerfil.setVisible(false);
            imageBotPerfil.setManaged(false);
        }
    }

    private Image carregarImagemCarta(Carta carta) {
        if (carta == null || carta.getImagem() == null || carta.getImagem().isBlank()) {
            return null;
        }

        String caminho = "/" + carta.getImagem();
        InputStream is = getClass().getResourceAsStream(caminho);

        if (is != null) {
            return new Image(is);
        }

        System.out.println("[IMG] Não encontrou imagem: " + caminho);
        return null;
    }

    private ImageView criarImagemCartaView(Carta carta, double largura, double altura) {
        Image imagem = carregarImagemCarta(carta);
        ImageView imageView = new ImageView();

        if (imagem != null) {
            imageView.setImage(imagem);
        }

        imageView.setFitWidth(largura);
        imageView.setFitHeight(altura);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        return imageView;
    }

    private String obterSeloElemento(Carta carta) {
        if (carta == null || carta.getElemento() == null) {
            return "◉";
        }

        return switch (carta.getElemento().name()) {
            case "FOGO" -> "🔥";
            case "AGUA" -> "💧";
            case "TERRA" -> "🪨";
            case "LUZ" -> "✦";
            case "PLANTA" -> "❀";
            case "AR" -> "🌀";
            default -> "◉";
        };
    }

    @FXML
    private void renderSe() {
        labelStatusTurno.setText("Você se rendeu.");
        DueloResultado resultado = new DueloResultado();
        resultado.vitoria = false;
        resultado.mensagem = "Você se rendeu.";
        encerrarBatalha(resultado);
    }
}