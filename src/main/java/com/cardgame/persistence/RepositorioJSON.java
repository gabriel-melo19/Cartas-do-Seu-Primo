package com.cardgame.persistence;

import com.cardgame.effects.EfeitoFactory;
import com.cardgame.model.Carta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RepositorioJSON {

    private static final String RESOURCE_PATH = "/com/cardgame/dados/cartas.json";
    private static final String SAVE_PATH = "saves/cartas.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Carta> listarCartas() {
        try (InputStream is = RepositorioJSON.class.getResourceAsStream(RESOURCE_PATH)) {

            if (is == null) {
                System.out.println("[ERRO] cartas.json não encontrado em: " + RESOURCE_PATH);
                return new ArrayList<>();
            }

            List<Carta> cartas = mapper.readValue(is, new TypeReference<List<Carta>>() {});

            for (Carta c : cartas) {
                if (c.getTipoEfeito() != null) {
                    c.setEfeito(EfeitoFactory.criarEfeito(c.getTipoEfeito(), null));
                }
            }

            return cartas;

        } catch (FileNotFoundException e) {
            System.err.println("[ERRO DE ARQUIVO] " + e.getMessage());
            return createEmptyList("cartas.json", RESOURCE_PATH);
        } catch (JsonProcessingException e) {
            System.err.println("[ERRO DE PARSE] JSON inválido em " + RESOURCE_PATH + ": " + e.getMessage());
            return createEmptyList("cartas.json", RESOURCE_PATH);
        } catch (IOException e) {
            System.err.println("[ERRO IO] Falha ao ler " + RESOURCE_PATH + ": " + e.getMessage());
            return createEmptyList("cartas.json", RESOURCE_PATH);
        } catch (RuntimeException e) {
            System.err.println("[ERRO INESPERADO] Falha ao criar efeito para carta: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private static List<Carta> createEmptyList(String descricao, String path) {
        System.out.println("[AVISO] Retornando lista vazia para: " + descricao);
        return new ArrayList<>();
    }
}