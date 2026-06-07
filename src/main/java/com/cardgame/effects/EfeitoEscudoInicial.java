package com.cardgame.effects;

import com.cardgame.model.Carta;
import com.cardgame.model.Jogador;

/**
 * Efeito que concede escudo inicial à carta quando colocada no tabuleiro.
 */

public class EfeitoEscudoInicial implements EfeitoCarta {

    public EfeitoEscudoInicial() {
    }

    @Override
    public void executarEfeito(Carta carta, Jogador jogadorOponente, Jogador jogadorProprietario, String faseDoJogo) {
        if ("INICIO_TABULEIRO".equals(faseDoJogo)) {
            carta.ativarEscudo();
        }
    }

    @Override
    public String getNomeEfeito() {
        return "Escudo: Protege 1 ataque";
    }

    @Override
    public boolean isPassivo() {
        return false;
    }
}
