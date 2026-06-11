package com.cardgame.model;

import com.cardgame.logic.SistemaCombate;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Objects;

/**
 * Representa um oponente controlado por IA.
 * Diferentes níveis de dificuldade alteram a estratégia de escolha de cartas.
 */
public class Bot extends Jogador {

    public enum Dificuldade {
        FACIL(1),
        MEDIO(2),
        DIFICIL(3);

        private final int nivel;
        Dificuldade(int nivel) { this.nivel = nivel; }
        public int getNivel() { return nivel; }
    }

    @JsonProperty("dificuldade")
    private final Dificuldade dificuldade;

    private static final Random RANDOM = new Random();
    private static int contadorBots = 0;

    public Bot(String nome, Deck deckInicial, Dificuldade dificuldade) {
        super("BOT_" + (++contadorBots), nome, deckInicial);
        this.dificuldade = Objects.requireNonNull(dificuldade, "Dificuldade não pode ser nula");
    }

    @Override
    public void executarAcoesDoTurno(Jogador oponente, SistemaCombate sistemaCombate) {
        iniciarNovoTurno();

        if (temCartaNoTabuleiro()) {
            return;
        }

        if (!getMao().isEmpty()) {
            int indiceEscolhido = escolherMelhorCarta(oponente);
            jogarNoTabuleiro(indiceEscolhido);
        }
    }

    /**
     * Decide qual carta jogar baseado na dificuldade.
     * Foco: Vantagem elemental (Difícil) ou poder bruto (Médio) ou Aleatório (Fácil).
     */
    private int escolherMelhorCarta(Jogador oponente) {
        Carta cartaOponente = oponente.getCartaNohTabuleiro();
        List<Carta> mao = getMao();

        switch (dificuldade) {
            case FACIL:
                return RANDOM.nextInt(mao.size());

            case MEDIO:
                return encontrarIndiceMaiorAtaque(mao);

            case DIFICIL:
                if (cartaOponente == null) {
                    return encontrarIndiceMaiorAtaque(mao);
                }

                for (int i = 0; i < mao.size(); i++) {
                    Carta c = mao.get(i);
                    if (c.getElemento().ehForteContra(cartaOponente.getElemento())) {
                        return i;
                    }
                }
                return encontrarIndiceMaiorAtaque(mao);

            default:
                return 0;
        }
    }

    private int encontrarIndiceMaiorAtaque(List<Carta> mao) {
        int indiceMelhor = 0;
        int maiorAtaque = -1;

        for (int i = 0; i < mao.size(); i++) {
            if (mao.get(i).getPoderDeLutaBase() > maiorAtaque) {
                maiorAtaque = mao.get(i).getPoderDeLutaBase();
                indiceMelhor = i;
            }
        }
        return indiceMelhor;
    }

    /**
     * Gera a lista de prêmios ao ser derrotado.
     * Regra: Solta 1 ou 2 cartas ALEATÓRIAS da SUA PRÓPRIA MÃO ATUAL.
     *
     * @return Lista de cartas que o jogador humano ganhou.
     */
    public List<Carta> gerarRecompensa() {
        List<Carta> premios = new ArrayList<>();
        List<Carta> maoDispo = getMao();

        if (maoDispo.isEmpty()) {
            return premios;
        }

        int qtd = (RANDOM.nextBoolean()) ? 1 : 2;

        qtd = Math.min(qtd, maoDispo.size());

        Collections.shuffle(maoDispo, RANDOM);

        for (int i = 0; i < qtd; i++) {
            premios.add(maoDispo.get(i));
        }

        return premios;
    }

    public Dificuldade getDificuldade() { return dificuldade; }
}