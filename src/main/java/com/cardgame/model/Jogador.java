package com.cardgame.model;

import com.cardgame.logic.SistemaCombate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Classe abstrata base para Jogadores (Humano e Bot).
 * Gerencia mão, tabuleiro, ciclo de vida da carta e regra de swap.
 */
public abstract class Jogador {

    @JsonProperty("id")
    private String id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("colecao")
    private List<Carta> colecao;

    @JsonIgnore
    private Deck deckReferencia;

    @JsonProperty("mao")
    private List<Carta> mao;

    @JsonIgnore
    private Carta cartaNoTabuleiro;

    @JsonIgnore
    private boolean trocaRealizadaNesteTurno;

    /**
     * Construtor protegido. Inicializa o estado padrão do jogador.
     * Cria a mão copiando até 5 cartas do deck fornecido.
     */
    protected Jogador(String id, String nome, Deck deckInicial) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.deckReferencia = Objects.requireNonNull(deckInicial, "Deck inicial não pode ser nulo");

        this.colecao = new ArrayList<>();

        List<Carta> cartasIniciais = deckInicial.getCartas().stream()
                .limit(5)
                .collect(Collectors.toList());
        this.mao = new ArrayList<>(cartasIniciais);

        this.cartaNoTabuleiro = null;
        this.trocaRealizadaNesteTurno = false;
    }

    /**
     * Deve ser chamado no início de CADA turno.
     * Reseta a flag de troca, permitindo que o jogador realize uma nova substituição.
     */
    public void iniciarNovoTurno() {
        this.trocaRealizadaNesteTurno = false;
    }

    /**
     * Tenta jogar uma carta da mão para o tabuleiro.
     *
     * Regras implementadas:
     * 1. Se o tabuleiro está vazio: A carta é colocada diretamente.
     * 2. Se há carta no tabuleiro E NÃO trocou este turno:
     *    - Remove a nova carta da mão.
     *    - Devolve a carta antiga para o FINAL da mão (regra validada).
     *    - Coloca a nova carta no tabuleiro.
     *    - Marca a troca como usada.
     * 3. Se há carta no tabuleiro E JÁ trocou este turno: Retorna erro.
     *
     * @param indiceMao Índice da carta na lista 'mao' (0 a tamanho-1).
     * @return O resultado da jogada (SUCESSO, TROCA_REALIZADA, etc).
     */
    public JogadaResultado jogarNoTabuleiro(int indiceMao) {
        if (indiceMao < 0 || indiceMao >= mao.size()) {
            return JogadaResultado.ERRO_INDICE_INVALIDO;
        }

        if (cartaNoTabuleiro == null) {
            Carta cartaJogada = mao.remove(indiceMao);
            cartaNoTabuleiro = cartaJogada;

            cartaJogada.resetarStatus();

            if (cartaJogada.getTipoEfeito() != null) {
                cartaJogada.aplicarEfeito(null, this, "INICIO_TABULEIRO");
            }

            return JogadaResultado.SUCESSO;
        }

        if (!this.trocaRealizadaNesteTurno) {
            Carta cartaAntiga = cartaNoTabuleiro;
            Carta cartaNova = mao.remove(indiceMao);

            mao.add(cartaAntiga);

            cartaNoTabuleiro = cartaNova;
            cartaNova.resetarStatus();

            if (cartaNova.getTipoEfeito() != null) {
                cartaNova.aplicarEfeito(null, this, "INICIO_TABULEIRO");
            }

            this.trocaRealizadaNesteTurno = true;
            return JogadaResultado.TROCA_REALIZADA;
        }

        return JogadaResultado.TROCA_JA_USADA;
    }

    /**
     * Aplica dano à carta que está no tabuleiro deste jogador.
     * Se a vida chegar a 0 ou menos, a carta é REMOVIDA PERMANENTEMENTE do duelo.
     *
     * @param valorDano Quantidade de dano a receber.
     * @return true se a carta foi destruída, false se sobreviveu.
     */
    public boolean receberDano(int valorDano) {
        if (cartaNoTabuleiro == null || valorDano <= 0) {
            return false;
        }

        boolean foiDestruida = cartaNoTabuleiro.receberDano(valorDano);

        if (foiDestruida || cartaNoTabuleiro.getVidaAtual() <= 0) {
            cartaNoTabuleiro = null;
            return true;
        }

        return false;
    }

    /**
     * Verifica se o jogador perdeu tudo.
     * Condição de derrota: Não tem cartas na mão E não tem carta no tabuleiro.
     *
     * @return true se perdeu, false caso contrário.
     */
    public boolean perdeuTudo() {
        return mao.isEmpty() && !temCartaNoTabuleiro();
    }
    

    public String getId() { return id; }
    public String getNome() { return nome; }
    public List<Carta> getColecao() { return new ArrayList<>(colecao); }
    public Deck getDeckReferencia() { return deckReferencia; }
    public List<Carta> getMao() { return new ArrayList<>(mao); }
    public int getQuantidadeNaMao() { return mao.size(); }
    public Carta getCartaNohTabuleiro() { return cartaNoTabuleiro; }
    public boolean temCartaNoTabuleiro() { return cartaNoTabuleiro != null; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jogador)) return false;
        Jogador jogador = (Jogador) o;
        return Objects.equals(id, jogador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s [Mão: %d | Tabuleiro: %s | Troca Usada: %b]",
                nome,
                mao.size(),
                cartaNoTabuleiro != null ? cartaNoTabuleiro.getNome() : "-",
                trocaRealizadaNesteTurno);
    }

    public abstract void executarAcoesDoTurno(Jogador oponente, SistemaCombate sistemaCombate);

}