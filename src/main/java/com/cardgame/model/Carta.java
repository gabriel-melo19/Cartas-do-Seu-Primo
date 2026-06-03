package com.cardgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Carta {

    @JsonProperty("id")
    private String id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("imagem")
    private String imagem;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("elemento")
    private Elemento elemento;

    @JsonProperty("poderDeLuta")
    private int poderDeLuta;

    @JsonProperty("vida")
    private int vida;

    @JsonProperty("efeito")
    private String efeito;


    // Jackson precisa desse construtor vazio para criar objetos ao ler o arquivo JSON. Definir apenas construtores com parâmetros, o salvamento/carregamento falhará.
    public Carta() {
    }

    public Carta(String id, String nome, String imagem, String descricao, Elemento elemento, int poderDeLuta,
                 int vida, String efeito) {
        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
        this.descricao = descricao;
        this.elemento = elemento;
        this.poderDeLuta = poderDeLuta;
        this.vida = vida;
        this.efeito = efeito;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Elemento getElemento() { return elemento; }
    public void setElemento(Elemento elemento) { this.elemento = elemento; }

    public int getPoderDeLuta() { return poderDeLuta; }
    public void setPoderDeLuta(int poderDeLuta) { this.poderDeLuta = poderDeLuta; }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }

    public String getEfeito() { return efeito; }
    public void setEfeito(String efeito) { this.efeito = efeito; }


    // Método utilitário para clonar a carta (útil para o jogo)
    public Carta clone() {
        return new Carta(this.id, this.nome, this.imagem, this.descricao, this.elemento, this.poderDeLuta,
                this.vida, this.efeito);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) | ATK: %d | HP: %d", this.nome, this.elemento,
                this.poderDeLuta, this.vida);
    }
}
