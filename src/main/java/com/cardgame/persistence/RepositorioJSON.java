package com.cardgame.persistence;

import com.cardgame.model.Carta;
import com.cardgame.model.TipoEfeito;
import com.cardgame.effects.EfeitoFactory;
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

            List<Carta> cartas = mapper.readValue(
                    file,
                    new TypeReference<List<Carta>>() {}
            );

            //CRIA OS EFEITOS DEPOIS DE LER
            for (Carta c : cartas) {
                if (c.getTipoEfeito() != null) {
                    c.setEfeito(
                            EfeitoFactory.criarEfeito(c.getTipoEfeito(), null)
                    );
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
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), cartas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void adicionarCarta(Carta carta) {
        List<Carta> cartas = listarCartas();
        cartas.add(carta);
        salvarCartas(cartas);
    }

    //TESTES
    public static void main(String[] args) {

        Carta carta = new Carta(
                "1",
                "Dragão Branco",
                "dragon.png",
                "Um dragão lendário",
                null,
                3000,
                2500,
                TipoEfeito.ESCUDO_INICIAL
        );

        adicionarCarta(carta);

        System.out.println("Cartas carregadas:");

        for (Carta c : listarCartas()) {
            System.out.println(c.getNome());

            if (c.temEfeito()) {
                System.out.println("Efeito: " + c.getEfeito().getNomeEfeito());
            }
        }
    }
}