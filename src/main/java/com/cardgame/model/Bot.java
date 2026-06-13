package com.cardgame.model;

import com.cardgame.logic.SistemaCombate;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.Random;

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

    @JsonProperty("descricao")
    private final String descricao;

    @JsonProperty("imagem")
    private final String imagem;

    private static final Random RANDOM = new Random();

    public Bot(String id, String nome, String descricao, String imagem, Deck deckInicial, Dificuldade dificuldade) {
        super(
                Objects.requireNonNull(id, "ID não pode ser nulo"),
                Objects.requireNonNull(nome, "Nome não pode ser nulo"),
                Objects.requireNonNull(deckInicial, "Deck inicial não pode ser nulo")
        );
        this.descricao = Objects.requireNonNull(descricao, "Descrição não pode ser nula");
        this.imagem = Objects.requireNonNull(imagem, "Imagem não pode ser nula");
        this.dificuldade = Objects.requireNonNull(dificuldade, "Dificuldade não pode ser nula");
    }

    @Override
    public void executarAcoesDoTurno(Jogador oponente, SistemaCombate sistemaCombate) {
        iniciarNovoTurno();

        List<Carta> maoAtual = getMao();
        if (maoAtual.isEmpty() && !temCartaNoTabuleiro()) {
            return;
        }

        if (!temCartaNoTabuleiro()) {
            if (!maoAtual.isEmpty()) {
                int indiceEscolhido = escolherMelhorCarta(oponente);
                jogarNoTabuleiro(indiceEscolhido);
            }
            return;
        }

        if (maoAtual.isEmpty()) {
            return;
        }

        int indiceMelhorDaMao = escolherMelhorCarta(oponente);
        Carta cartaAtualCampo = getCartaNohTabuleiro();
        Carta melhorCartaDaMao = maoAtual.get(indiceMelhorDaMao);

        if (deveTrocarCarta(cartaAtualCampo, melhorCartaDaMao, oponente)) {
            jogarNoTabuleiro(indiceMelhorDaMao);
        }
    }

    /**
     * Decide qual carta jogar baseado na dificuldade.
     * Foco: aleatoriedade (Fácil), poder bruto (Médio) ou heurística de confronto (Difícil).
     */
    private int escolherMelhorCarta(Jogador oponente) {
        List<Carta> mao = getMao();
        Carta cartaOponente = oponente != null ? oponente.getCartaNohTabuleiro() : null;

        if (mao.isEmpty()) {
            return 0;
        }

        switch (dificuldade) {
            case FACIL:
                return RANDOM.nextInt(mao.size());

            case MEDIO:
                return encontrarIndiceMelhorPontuacaoSimples(mao);

            case DIFICIL:
                return encontrarIndiceMelhorPontuacaoContraOponente(mao, cartaOponente);

            default:
                return 0;
        }
    }

    private boolean deveTrocarCarta(Carta cartaAtualCampo, Carta melhorCartaDaMao, Jogador oponente) {
        if (cartaAtualCampo == null || melhorCartaDaMao == null) {
            return false;
        }

        Carta cartaOponente = oponente != null ? oponente.getCartaNohTabuleiro() : null;

        switch (dificuldade) {
            case FACIL:
                return cartaAtualCampo.getVidaAtual() <= 20 && RANDOM.nextBoolean();

            case MEDIO:
                return melhorCartaDaMao.getPoderDeLutaAtual() > cartaAtualCampo.getPoderDeLutaAtual()
                        && melhorCartaDaMao.getVidaAtual() >= cartaAtualCampo.getVidaAtual();

            case DIFICIL:
                int pontuacaoAtual = calcularPontuacaoCarta(cartaAtualCampo, cartaOponente);
                int pontuacaoNova = calcularPontuacaoCarta(melhorCartaDaMao, cartaOponente);

                if (cartaAtualCampo.getVidaAtual() <= 15 && melhorCartaDaMao.getVidaAtual() > cartaAtualCampo.getVidaAtual()) {
                    return true;
                }

                return pontuacaoNova > pontuacaoAtual + 8;

            default:
                return false;
        }
    }

    private int encontrarIndiceMelhorPontuacaoSimples(List<Carta> mao) {
        int indiceMelhor = 0;
        int melhorPontuacao = Integer.MIN_VALUE;

        for (int i = 0; i < mao.size(); i++) {
            Carta carta = mao.get(i);
            int pontuacao = carta.getPoderDeLutaAtual() + carta.getVidaAtual();

            if (pontuacao > melhorPontuacao) {
                melhorPontuacao = pontuacao;
                indiceMelhor = i;
            }
        }

        return indiceMelhor;
    }

    private int encontrarIndiceMelhorPontuacaoContraOponente(List<Carta> mao, Carta cartaOponente) {
        int indiceMelhor = 0;
        int melhorPontuacao = Integer.MIN_VALUE;

        for (int i = 0; i < mao.size(); i++) {
            Carta carta = mao.get(i);
            int pontuacao = calcularPontuacaoCarta(carta, cartaOponente);

            if (pontuacao > melhorPontuacao) {
                melhorPontuacao = pontuacao;
                indiceMelhor = i;
            }
        }

        return indiceMelhor;
    }

    private int calcularPontuacaoCarta(Carta carta, Carta cartaOponente) {
        if (carta == null) {
            return Integer.MIN_VALUE;
        }

        int pontuacao = 0;

        pontuacao += carta.getPoderDeLutaAtual() * 2;
        pontuacao += carta.getVidaAtual();

        if (cartaOponente != null && carta.getElemento() != null && cartaOponente.getElemento() != null) {
            if (carta.getElemento().ehForteContra(cartaOponente.getElemento())) {
                pontuacao += 25;
            } else if (carta.getElemento().ehFracoContra(cartaOponente.getElemento())) {
                pontuacao -= 18;
            }
        }

        if (carta.getVidaAtual() <= 15) {
            pontuacao -= 20;
        }

        return pontuacao;
    }


    public Dificuldade getDificuldade() { return dificuldade; }
    public String getDescricao() { return descricao; }
    public String getImagem() { return imagem; }
}