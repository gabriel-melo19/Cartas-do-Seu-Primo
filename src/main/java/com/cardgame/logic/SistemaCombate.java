package com.cardgame.logic;

import com.cardgame.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Orquestra o fluxo de batalha entre Humano e Bot.
 * Responsável por aplicar regras, calcular dano e determinar vencedor.
 */
public class SistemaCombate {

    private final JogadorHumano humano;
    private final Bot bot;

    public SistemaCombate(JogadorHumano humano, Bot bot) {
        this.humano = humano;
        this.bot = bot;
    }

    /**
     * Chamar no início de cada turno do Jogador Humano.
     * Reseta flags de ambos os jogadores.
     */
    public void inicioDoTurnoHumano() {
        humano.iniciarNovoTurno();
        bot.iniciarNovoTurno();
    }

    /**
     * Executa a ação principal de "Passar Turno".
     * Fluxo:
     * 1. Humano já escolheu carta (feito antes de chamar este método).
     * 2. Verifica se Bot precisa jogar carta (se estiver sem carta no tabuleiro).
     * 3. Aplica modificadores elementares (Fortes/Fracos).
     * 4. Resolve combate simultâneo (Ambos atacam).
     * 5. Remove cartas mortas.
     * 6. Verifica condição de vitória/derrota.
     * 7. Se venceu, gera recompensa.
     */
    public DueloResultado passarTurno() {
        DueloResultado resultado = new DueloResultado();

        if (!bot.temCartaNoTabuleiro()) {
            bot.executarAcoesDoTurno(humano, this);
        }

        if (humano.perdeuTudo() || bot.perdeuTudo()) {
            verificarVitoria(resultado);
            return resultado;
        }

        if (humano.temCartaNoTabuleiro() && bot.temCartaNoTabuleiro()) {
            Carta cartaH = humano.getCartaNohTabuleiro();
            Carta cartaB = bot.getCartaNohTabuleiro();

            cartaH.aplicarModificadoresElementares(cartaB);
            cartaB.aplicarModificadoresElementares(cartaH);

            int danoParaBot = cartaH.getPoderDeLutaAtual();
            int danoParaHumano = cartaB.getPoderDeLutaAtual();

            boolean botMorreu = humano.receberDano(danoParaBot);
            boolean humanoMorreu = bot.receberDano(danoParaHumano);

            if (botMorreu || humanoMorreu) {
                resultado.batalhouComSucesso = true;
            }
        }

        // --- FASE 4: VERIFICAÇÃO DE RESULTADO ---
        verificarVitoria(resultado);

        return resultado;
    }

    /**
     * Verifica quem venceu e prepara a recompensa.
     */
    private void verificarVitoria(DueloResultado resultado) {
        if (humano.perdeuTudo()) {
            resultado.vitoria = false;
            resultado.mensagem = "Você ficou sem cartas! Derrota.";
        } else if (bot.perdeuTudo()) {
            resultado.vitoria = true;
            resultado.mensagem = "Vitória! Oponente sem cartas.";

            resultado.valorDinheiro = 50 * bot.getDificuldade().getNivel();

            List<Carta> cartasGanhas = bot.gerarRecompensa();
            resultado.cartasGanhas.addAll(cartasGanhas);

            for (Carta c : cartasGanhas) {
                humano.adicionarCartaPremio(c);
            }
        } else {
            resultado.vitoria = false; // Jogo continua
            resultado.mensagem = "Combate concluído. Próximo turno.";
            resultado.valorDinheiro = 0;
        }
    }
}

