package com.cardgame.persistence;

import com.cardgame.logic.SaveData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSave {

    private static final String PASTA_SAVES = "saves";
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private RepositorioSave() {
    }

    private static Path getPastaSavesPath() throws IOException {
        Path pasta = Path.of(PASTA_SAVES);
        if (!Files.exists(pasta)) {
            Files.createDirectories(pasta);
        }
        return pasta;
    }

    private static String normalizarNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return "save_sem_nome";
        }

        return nickname
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9-_]", "_");
    }

    private static Path getCaminhoSave(String nickname) throws IOException {
        String nomeArquivo = normalizarNickname(nickname) + ".json";
        return getPastaSavesPath().resolve(nomeArquivo);
    }

    public static void salvar(SaveData saveData) {
        if (saveData == null) {
            return;
        }

        try {
            Path caminho = getCaminhoSave(saveData.getNickname());
            MAPPER.writeValue(caminho.toFile(), saveData);
        } catch (IOException e) {
            System.out.println("[SAVE] Erro ao salvar progresso: " + e.getMessage());
        }
    }

    public static SaveData carregar(String nickname) {
        try {
            Path caminho = getCaminhoSave(nickname);

            if (!Files.exists(caminho)) {
                return null;
            }

            return MAPPER.readValue(caminho.toFile(), SaveData.class);
        } catch (IOException e) {
            System.out.println("[SAVE] Erro ao carregar save: " + e.getMessage());
            return null;
        }
    }

    public static boolean existeSave(String nickname) {
        try {
            Path caminho = getCaminhoSave(nickname);
            return Files.exists(caminho);
        } catch (IOException e) {
            return false;
        }
    }

    public static List<SaveData> listarSaves() {
        List<SaveData> saves = new ArrayList<>();

        try {
            Path pasta = getPastaSavesPath();

            Files.list(pasta)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        try {
                            SaveData save = MAPPER.readValue(path.toFile(), SaveData.class);
                            saves.add(save);
                        } catch (IOException e) {
                            System.out.println("[SAVE] Erro ao ler arquivo: " + path.getFileName());
                        }
                    });

        } catch (IOException e) {
            System.out.println("[SAVE] Erro ao listar saves: " + e.getMessage());
        }

        return saves;
    }

    public static void deletar(String nickname) {
        try {
            Path caminho = getCaminhoSave(nickname);
            Files.deleteIfExists(caminho);
        } catch (IOException e) {
            System.out.println("[SAVE] Erro ao deletar save: " + e.getMessage());
        }
    }
}