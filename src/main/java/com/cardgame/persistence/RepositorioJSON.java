package com.cardgame.persistence;

import com.cardgame.model.Carta;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RepositorioJSON {

    private static final String FILE_PATH = "cartas.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Carta> listarCartas() {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(file, new TypeReference<List<Carta>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void salvarCartas(List<Carta> cartas) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), cartas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void adicionarCarta(Carta carta) {
        List<Carta> cartas = listarCartas();
        cartas.add(carta);
        salvarCartas(cartas);
    }

    // Só pra teste
    public static void main(String[] args) {

        Carta carta = new Carta(
                "1",
                "Dragão Branco",
                "dragon.png",
                "Um dragão lendário",
                null,
                3000,
                2500,
                null    // <- deixei null para não commitar o erro. Esse é o efeito, mas os efeitos precisam ser objetos (não uma lista de objetos)
        );

        adicionarCarta(carta);

        for (Carta c : listarCartas()) {
            System.out.println(c.getNome());
        }
    }
}