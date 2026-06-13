package com.cardgame.ui.controller;

import com.cardgame.logic.SessaoJogo;
import com.cardgame.ui.ControladorDeFluxo;
import com.cardgame.ui.ScreenManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarregarSaveController implements ControladorDeFluxo {

    @FXML private Label tituloTela;
    @FXML private Label labelStatus;
    @FXML private ListView<String> listaSaves;
    @FXML private Button botaoCarregar;
    @FXML private Button botaoVoltar;

    private ScreenManager screenManager;

    @Override
    public void configurar(ScreenManager screenManager) {
        this.screenManager = screenManager;
        carregarListaDeSaves();
    }

    private void carregarListaDeSaves() {
        Path pastaSaves = Path.of("saves");

        try {
            if (!Files.exists(pastaSaves)) {
                Files.createDirectories(pastaSaves);
            }

            List<String> saves;
            try (Stream<Path> stream = Files.list(pastaSaves)) {
                saves = stream
                        .filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .filter(nome -> nome.endsWith(".json"))
                        .map(nome -> nome.substring(0, nome.length() - 5))
                        .sorted()
                        .collect(Collectors.toList());
            }

            listaSaves.setItems(FXCollections.observableArrayList(saves));

            if (saves.isEmpty()) {
                tituloTela.setText("Escolha seu Save");
                labelStatus.setText("Nenhum save encontrado.");
                if (botaoCarregar != null) {
                    botaoCarregar.setDisable(true);
                }
            } else {
                tituloTela.setText("Escolha seu Save");
                labelStatus.setText("Selecione um save da lista.");
                listaSaves.getSelectionModel().selectFirst();
            }

        } catch (IOException e) {
            tituloTela.setText("Escolha seu Save");
            labelStatus.setText("Erro ao listar saves.");
            if (botaoCarregar != null) {
                botaoCarregar.setDisable(true);
            }
            System.out.println("[SAVE] Erro ao listar saves: " + e.getMessage());
        }
    }

    @FXML
    public void carregarSaveSelecionado() {
        String saveSelecionado = listaSaves.getSelectionModel().getSelectedItem();

        if (saveSelecionado == null || saveSelecionado.isBlank()) {
            labelStatus.setText("Selecione um save primeiro.");
            if (botaoCarregar != null) {
                botaoCarregar.setDisable(true);
            }
            return;
        }

        SessaoJogo.setNicknameAtual(saveSelecionado);
        SessaoJogo.setCarregandoDeSave(true);

        labelStatus.setText("Carregando save: " + saveSelecionado + "...");
        System.out.println("[SAVE] Save carregado: " + saveSelecionado);

        if (screenManager != null) {
            screenManager.navegarPara("/com/cardgame/fxml/selecao_adversario.fxml");
        }
    }

    @FXML
    public void voltarParaMenu() {
        if (screenManager != null) {
            screenManager.navegarPara("/com/cardgame/fxml/menu_principal.fxml");
        }
    }
}