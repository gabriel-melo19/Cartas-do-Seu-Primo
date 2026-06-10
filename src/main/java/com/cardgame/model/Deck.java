package com.cardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Deck {

    private final String id;
    private final String nome;
    private final String descricao;
    private final List<Carta> cartas;

    public Deck(String id, String nome, String descricao, List<Carta> cartas) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "nome não pode ser nulo");
        this.descricao = Objects.requireNonNull(descricao, "descricao não pode ser nula");
        this.cartas = new ArrayList<>(Objects.requireNonNull(cartas, "cartas não pode ser nula"));
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Carta> getCartas() {
        return Collections.unmodifiableList(cartas);
    }

    public int getQuantidadeCartas() {
        return cartas.size();
    }

    public boolean estaVazio() {
        return cartas.isEmpty();
    }

    public List<Carta> getPrimeirasCartas(int quantidade) {
        if (quantidade <= 0 || cartas.isEmpty()) {
            return List.of();
        }

        int limite = Math.min(quantidade, cartas.size());
        return new ArrayList<>(cartas.subList(0, limite));
    }

    public boolean contemCarta(Carta carta) {
        return cartas.contains(carta);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", quantidadeCartas=" + cartas.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deck)) return false;
        Deck deck = (Deck) o;
        return Objects.equals(id, deck.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}