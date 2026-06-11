package com.cardgame.model;

import com.cardgame.logic.DueloResultado;
import com.cardgame.logic.SistemaCombate;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa o jogador humano.
 * Responsável por interagir via UI e receber prêmios.
 */
public class JogadorHumano extends Jogador {

    @JsonProperty("fotoCaminho")
    private String fotoCaminho;

    @JsonProperty("nomeDeckPersonalizado")
    private String nomeDeckPersonalizado;

    public JogadorHumano(String id, String nome, Deck deckInicial, String fotoCaminho) {
        super(id, nome, deckInicial);
        this.fotoCaminho = Objects.requireNonNullElse(fotoCaminho, "");
        this.nomeDeckPersonalizado = nome;
    }

    /**
     * O humano não executa ações automáticas.
     * As ações são disparadas pelos botões da Interface Gráfica (Pessoa 2).
     * Este método permanece vazio ou pode ser usado para logs de depuração.
     * Humano esperará ação manual da UI
     */
    @Override
    public void executarAcoesDoTurno(Jogador oponente, SistemaCombate sistemaCombate) {}

    /**
     * Processa a recompensa de uma vitória.
     * Adiciona dinheiro e cartes ganhos à coleção.
     */
    public void receberRecompensa(DueloResultado resultado) {
        if (resultado.vitoria) {
            for (Carta c : resultado.cartasGanhas) {
                adicionarCartaPremio(c);
            }
        }
    }

    public String getFotoCaminho() { return fotoCaminho; }
    public String getNomeDeckPersonalizado() { return nomeDeckPersonalizado; }
    public void setNomeDeckPersonalizado(String nomeDeckPersonalizado) {
        this.nomeDeckPersonalizado = nomeDeckPersonalizado;
    }

    // Nota: A edição real do deck (adicionar/remover cartas) será feita
    // pela classe Controller/UI chamando métodos diretos ou criando um novo Deck.
}