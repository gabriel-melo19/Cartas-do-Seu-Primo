package com.cardgame.persistence;

import com.cardgame.model.Bot;
import com.cardgame.model.Carta;
import com.cardgame.model.Deck;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RepositorioBots {

    private static final String RESOURCE_PATH = "/com/cardgame/dados/bots.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Bot> listarBots() {
        List<Bot> bots = new ArrayList<>();

        try (InputStream is = RepositorioBots.class.getResourceAsStream(RESOURCE_PATH)) {

            if (is == null) {
                System.out.println("[ERRO] bots.json não encontrado em: " + RESOURCE_PATH);
                return bots;
            }

            List<Carta> cartasDisponiveis = RepositorioJSON.listarCartas();
            Map<String, Carta> cartasPorId = mapearCartasPorId(cartasDisponiveis);

            JsonNode raiz = mapper.readTree(is);

            for (JsonNode botNode : raiz) {
                String botId = botNode.get("id").asText();
                String nome = botNode.get("nome").asText();
                String descricao = botNode.get("descricao").asText();
                String imagem = botNode.get("imagem").asText();
                Bot.Dificuldade dificuldade = Bot.Dificuldade.valueOf(
                        botNode.get("dificuldade").asText()
                );

                JsonNode deckNode = botNode.get("deck");
                String deckId = deckNode.get("id").asText();
                String deckNome = deckNode.get("nome").asText();
                String deckDescricao = deckNode.get("descricao").asText();

                List<Carta> cartasDeck = new ArrayList<>();
                JsonNode cartasIdsNode = deckNode.get("cartasIds");

                for (JsonNode cartaIdNode : cartasIdsNode) {
                    String cartaId = cartaIdNode.asText();
                    Carta cartaEncontrada = cartasPorId.get(cartaId);

                    if (cartaEncontrada != null) {
                        cartasDeck.add(cartaEncontrada);
                    } else {
                        System.out.println("[AVISO] Carta com id " + cartaId + " não encontrada para o bot " + nome);
                    }
                }

                Deck deckBot = new Deck(deckId, deckNome, deckDescricao, cartasDeck);

                Bot bot = new Bot(
                        botId,
                        nome,
                        descricao,
                        imagem,
                        deckBot,
                        dificuldade
                );

                bots.add(bot);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bots;
    }

    private static Map<String, Carta> mapearCartasPorId(List<Carta> cartas) {
        Map<String, Carta> mapa = new LinkedHashMap<>();

        for (Carta carta : cartas) {
            mapa.put(carta.getId(), carta);
        }

        return mapa;
    }
}