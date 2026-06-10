package com.cardgame.persistence;

import com.cardgame.effects.EfeitoFactory;
import com.cardgame.model.Carta;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void salvarCartas(List<Carta> cartas) {
        try {
            Path path = Path.of(SAVE_PATH);
            Files.createDirectories(path.getParent());
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), cartas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void adicionarCarta(Carta carta) {
        List<Carta> cartas = new ArrayList<>(listarCartas());
        cartas.add(carta);
        salvarCartas(cartas);
    }
}