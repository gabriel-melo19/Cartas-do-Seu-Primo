package com.cardgame.logic;

import com.cardgame.model.Bot;
import com.cardgame.model.Carta;
import com.cardgame.model.JogadorHumano;

/**
 * Orquestra o fluxo de batalha entre Humano e Bot.
 * Regras:
 * - Cada lado mantém ou troca sua carta antes do combate.
 * - A carta antiga volta para a mão com o HP atual, se houver troca.
 * - O combate é simultâneo: ambas as cartas causam dano no mesmo turno.
 * - Carta só sai do jogo quando morre.
 * - O jogo termina quando um lado fica sem cartas na mão e sem carta no tabuleiro.
 */
public class SistemaCombate {

    private final JogadorHumano humano;
    private final Bot bot;

    public SistemaCombate(JogadorHumano humano, Bot bot) {
        this.humano = humano;
        this.bot = bot;
    }

    /**
     * Chamar no início de cada turno do jogador humano.
     * Reseta a possibilidade de troca para ambos.
     */
    public void inicioDoTurnoHumano() {
        humano.iniciarNovoTurno();
        bot.iniciarNovoTurno();
    }

    /**
     * Resolve o turno completo:
     * 1. Bot decide se mantém ou troca carta.
     * 2. Se alguém ficou sem cartas, verifica fim de jogo.
     * 3. Se ambos têm carta no campo, resolve combate simultâneo.
     * 4. Remove cartas mortas.
     * 5. Verifica vitória/derrota.
     */
    public DueloResultado passarTurno() {
        DueloResultado resultado = new DueloResultado();
        resultado.vitoria = false;
        resultado.batalhouComSucesso = false;

        if (bot != null) {
            bot.executarAcoesDoTurno(humano, this);
        }

        if (humano.perdeuTudo()) {
            verificarVitoria(resultado);
            return resultado;
        }
        if (bot.perdeuTudo()) {
            verificarVitoria(resultado);
            return resultado;
        }

        if (!humano.temCartaNoTabuleiro() || !bot.temCartaNoTabuleiro()) {
            resultado.mensagem = "Necessário ter carta no campo para combater.";

            if (humano.perdeuTudo() || bot.perdeuTudo()) {
                verificarVitoria(resultado);
            }
            return resultado;
        }

        Carta cartaHumano = humano.getCartaNohTabuleiro();
        Carta cartaBot = bot.getCartaNohTabuleiro();

        int danoParaBot = calcularDano(cartaHumano, cartaBot);
        int danoParaHumano = calcularDano(cartaBot, cartaHumano);

        boolean botMorreu = bot.receberDano(danoParaBot);
        boolean humanoMorreu = humano.receberDano(danoParaHumano);

        resultado.batalhouComSucesso = true;
        resultado.mensagem = construirMensagemCombate(
                cartaHumano,
                cartaBot,
                danoParaBot,
                danoParaHumano,
                botMorreu,
                humanoMorreu
        );

        verificarVitoria(resultado);

        if (!humano.perdeuTudo() && !bot.perdeuTudo()) {
            if (resultado.mensagem == null || resultado.mensagem.isBlank()) {
                resultado.mensagem = "Combate concluído. Próximo turno.";
            }
            inicioDoTurnoHumano();
        }

        return resultado;
    }

    /**
     * Calcula o dano de uma carta atacante sobre uma defensora.
     * Usa o ATK atual e aplica um bônus/penalidade simples por elemento.
     */
    private int calcularDano(Carta atacante, Carta defensora) {
        if (atacante == null) {
            return 0;
        }

        int dano = atacante.getPoderDeLutaAtual();

        if (defensora != null
                && atacante.getElemento() != null
                && defensora.getElemento() != null) {

            if (atacante.getElemento().ehForteContra(defensora.getElemento())) {
                dano += 10;
            } else if (atacante.getElemento().ehFracoContra(defensora.getElemento())) {
                dano -= 10;
            }
        }

        return Math.max(dano, 0);
    }

    private String construirMensagemCombate(
            Carta cartaHumanoAntes,
            Carta cartaBotAntes,
            int danoParaBot,
            int danoParaHumano,
            boolean botMorreu,
            boolean humanoMorreu
    ) {
        String nomeHumano = cartaHumanoAntes != null ? cartaHumanoAntes.getNome() : "Sua carta";
        String nomeBot = cartaBotAntes != null ? cartaBotAntes.getNome() : "Carta do bot";

        if (humanoMorreu && botMorreu) {
            return nomeHumano + " e " + nomeBot + " foram derrotadas no combate.";
        }

        if (botMorreu) {
            return nomeHumano + " causou " + danoParaBot + " de dano e derrotou " + nomeBot + ".";
        }

        if (humanoMorreu) {
            return nomeBot + " causou " + danoParaHumano + " de dano e derrotou " + nomeHumano + ".";
        }

        return nomeHumano + " causou " + danoParaBot + " e recebeu " + danoParaHumano + " de dano.";
    }

    /**
     * Verifica quem venceu e prepara a recompensa.
     */
    private void verificarVitoria(DueloResultado resultado) {
        if (humano.perdeuTudo() && bot.perdeuTudo()) {
            resultado.vitoria = false;
            resultado.valorDinheiro = 0;
            resultado.mensagem = "As duas equipes ficaram sem cartas. Empate.";
            return;
        }

        if (humano.perdeuTudo()) {
            resultado.vitoria = false;
            resultado.valorDinheiro = 0;
            resultado.mensagem = "Você ficou sem cartas! Derrota.";
            return;
        }

        if (bot.perdeuTudo()) {
            resultado.vitoria = true;
            resultado.mensagem = "Vitória! Oponente sem cartas.";
            resultado.valorDinheiro = 50 * bot.getDificuldade().getNivel();
            return;
        }

        resultado.vitoria = false;
        resultado.valorDinheiro = 0;
        if (resultado.mensagem == null || resultado.mensagem.isBlank()) {
            resultado.mensagem = "Combate concluído. Próximo turno.";
        }
    }

    public void botJogaCartaAutomaticamente() {
        if (bot != null && !bot.temCartaNoTabuleiro()) {
            bot.executarAcoesDoTurno(humano, this);
        }
    }
}