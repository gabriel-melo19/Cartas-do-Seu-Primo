package com.cardgame.effects;

import com.cardgame.model.Carta;
import com.cardgame.model.Jogador;

/**
 * Interface para todos os efeitos de cartas.
 * Implementa o padrão Strategy para permitir efeitos intercambiáveis.
 */

public interface EfeitoCarta {

    /**
     * aplica o efeito da carta.
     * @param jogadorProprietario O dono da carta (que ativa o efeito)
     * @param faseDoJogo Fase atual do jogo (INICIO, COMBATE, FIM)
     */
    void executarEfeito(Carta carta, Jogador jogadorOponente,
                  Jogador jogadorProprietario, String faseDoJogo);


    /**
     * Retorna o nome descritivo do efeito.
     * @return Nome do efeito para exibição na UI
     */
    String getNomeEfeito();

    /**
     * Verifica se o efeito é passivo (ativo constantemente) ou ativo (disparado manualmente).
     * @return true se for efeito passivo
     */
    boolean isPassivo();
}
