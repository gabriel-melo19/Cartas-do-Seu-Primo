package com.cardgame.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.cardgame.effects.EfeitoCarta;

import java.util.Objects;

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

    //VEM DO JSON
    @JsonProperty("tipoEfeito")
    private TipoEfeito tipoEfeito;

    //NÃO VAI PRO JSON
    @JsonIgnore
    private transient EfeitoCarta efeito;

    @JsonProperty("temEscudo")
    private boolean temEscudo;

    public Carta() {
    }

    public Carta(String id, String nome, String imagem, String descricao,
                 Elemento elemento, int poderDeLuta, int vida, TipoEfeito tipoEfeito) {

        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
        this.descricao = descricao;
        this.elemento = elemento;
        this.poderDeLuta = poderDeLuta;
        this.vida = vida;
        this.tipoEfeito = tipoEfeito;
        this.temEscudo = false;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getImagem() { return imagem; }
    public String getDescricao() { return descricao; }
    public Elemento getElemento() { return elemento; }
    public int getPoderDeLuta() { return poderDeLuta; }
    public int getVida() { return vida; }
    public TipoEfeito getTipoEfeito() { return tipoEfeito; }
    public EfeitoCarta getEfeito() { return efeito; }
    public boolean temEscudo() { return temEscudo; }

    public void setEfeito(EfeitoCarta efeito) {
        this.efeito = efeito;
    }

    public void ativarEscudo() {
        this.temEscudo = true;
    }

    public void removerEscudo() {
        this.temEscudo = false;
    }

    public boolean receberDano(int dano) {
        if (dano <= 0) return false;

        if (this.temEscudo) {
            this.removerEscudo();
            return true;
        }

        this.vida -= dano;
        return false;
    }

    public void aplicarEfeito(Jogador oponente, Jogador proprietario, String fase) {
        if (this.efeito != null) {
            this.efeito.executarEfeito(this, oponente, proprietario, fase);
        }
    }

    public boolean temEfeito() {
        return this.efeito != null;
    }

    @Override
    public String toString() {
        String efeitoStr = temEfeito() ? efeito.getNomeEfeito() : "Sem efeito";
        String escudoStr = temEscudo ? "[🛡️]" : "";

        return String.format("%s[%s] %s | ATK: %d | HP: %d | %s",
                escudoStr, this.nome, this.elemento,
                this.poderDeLuta, this.vida, efeitoStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carta)) return false;
        Carta carta = (Carta) o;
        return Objects.equals(id, carta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}