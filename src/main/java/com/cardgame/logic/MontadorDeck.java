package com.cardgame.logic;

import com.cardgame.model.Carta;
import com.cardgame.model.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MontadorDeck {

    private static final int QUANTIDADE_DECKS = 3;
    private static final int CARTAS_POR_DECK = 5;

    private final List<Carta> todasAsCartas;
    private final Random random = new Random();

    public MontadorDeck(List<Carta> todasAsCartas) {
        this.todasAsCartas = new ArrayList<>(todasAsCartas);
    }

    public List<Deck> montarDecksPadrao() {
        List<Deck> decks = new ArrayList<>();

        if (todasAsCartas.size() < CARTAS_POR_DECK) {
            return decks;
        }

        String[] nomesDecks = {
                "Deck Neymar",
                "Deck Pelé",
                "Deck Ronaldo"
        };

        String[] descricoes = {
                "Um deck imprevisível e ofensivo.",
                "Um deck clássico e equilibrado.",
                "Um deck forte para pressão constante."
        };

        for (int i = 0; i < QUANTIDADE_DECKS; i++) {
            List<Carta> cartasDeck = sortearCartasUnicas(CARTAS_POR_DECK);

            decks.add(new Deck(
                    "DECK_" + (i + 1),
                    nomesDecks[i],
                    descricoes[i],
                    cartasDeck
            ));
        }

        return decks;
    }

    private List<Carta> sortearCartasUnicas(int quantidade) {
        List<Carta> copia = new ArrayList<>(todasAsCartas);
        Collections.shuffle(copia, random);
        return new ArrayList<>(copia.subList(0, quantidade));
    }
}