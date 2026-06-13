package com.cardgame.logic;

import com.cardgame.model.Bot;
import com.cardgame.model.Carta;
import com.cardgame.model.Deck;
import com.cardgame.persistence.RepositorioBots;
import com.cardgame.persistence.RepositorioJSON;
import com.cardgame.persistence.RepositorioSave;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PreparadorBatalha {

    private PreparadorBatalha() {
    }

    public static Deck carregarDeckDoJogador(String nickname) {
        SaveData saveData = RepositorioSave.carregar(nickname);

        if (saveData == null || saveData.getCartasDoDeckSelecionado() == null || saveData.getCartasDoDeckSelecionado().isEmpty()) {
            return null;
        }

        List<Carta> cartasBase = RepositorioJSON.listarCartas();
        Map<String, Carta> cartasPorId = mapearCartasPorId(cartasBase);

        List<Carta> cartasDoDeck = new ArrayList<>();
        for (String cartaId : saveData.getCartasDoDeckSelecionado()) {
            Carta cartaBase = cartasPorId.get(cartaId);

            if (cartaBase != null) {
                cartasDoDeck.add(new Carta(cartaBase));
            } else {
                System.out.println("[BATALHA] Carta do jogador não encontrada no catálogo: " + cartaId);
            }
        }

        if (cartasDoDeck.isEmpty()) {
            return null;
        }

        String deckId = (saveData.getDeckIdSelecionado() != null && !saveData.getDeckIdSelecionado().isBlank())
                ? saveData.getDeckIdSelecionado()
                : "DECK_PLAYER";

        return new Deck(
                deckId,
                "Deck do Jogador",
                "Deck carregado do save",
                cartasDoDeck
        );
    }

    public static Bot carregarBotDaPartida(Bot botSelecionadoSessao) {
        if (botSelecionadoSessao == null || botSelecionadoSessao.getId() == null || botSelecionadoSessao.getId().isBlank()) {
            return null;
        }

        List<Bot> botsDisponiveis = RepositorioBots.listarBots();

        for (Bot botBase : botsDisponiveis) {
            if (botSelecionadoSessao.getId().equals(botBase.getId())) {
                Deck deckCopiado = botBase.getDeckReferencia().criarCopiaParaPartida();

                return new Bot(
                        botBase.getId(),
                        botBase.getNome(),
                        botBase.getDescricao(),
                        botBase.getImagem(),
                        deckCopiado,
                        botBase.getDificuldade()
                );
            }
        }

        System.out.println("[BATALHA] Bot não encontrado para o id: " + botSelecionadoSessao.getId());
        return null;
    }

    private static Map<String, Carta> mapearCartasPorId(List<Carta> cartas) {
        Map<String, Carta> mapa = new LinkedHashMap<>();

        for (Carta carta : cartas) {
            mapa.put(carta.getId(), carta);
        }

        return mapa;
    }
}