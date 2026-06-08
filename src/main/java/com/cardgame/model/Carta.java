package com.cardgame.model;

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

    @JsonProperty("efeito")
    private EfeitoCarta efeito;

    @JsonProperty("temEscudo")
    private boolean temEscudo;

    // Jackson precisa desse construtor vazio para criar objetos ao ler o arquivo JSON. Definir apenas construtores com parâmetros, o salvamento/carregamento falhará.
    public Carta() {
    }

    public Carta(String id, String nome, String imagem, String descricao, Elemento elemento, int poderDeLuta,
                 int vida, EfeitoCarta efeito) {
        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
        this.descricao = descricao;
        this.elemento = elemento;
        this.poderDeLuta = poderDeLuta;
        this.vida = vida;
        this.efeito = efeito;

        this.temEscudo = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    public int getPoderDeLuta() {
        return poderDeLuta;
    }

    public void setPoderDeLuta(int poderDeLuta) {
        this.poderDeLuta = poderDeLuta;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public EfeitoCarta getEfeito() {
        return efeito;
    }

    public void setEfeito(EfeitoCarta efeito) {
        this.efeito = efeito;
    }

    public boolean temEscudo() {
        return temEscudo;
    }

    public void setTemEscudo(boolean temEscudo) {
        this.temEscudo = temEscudo;
    }


    /**
     * Ativa o escudo da carta.
     * O escudo protegerá a carta de UM ataque completo e desaparecerá.
     */
    public void ativarEscudo() {
        this.temEscudo = true;
    }

    /**
     * Remove o escudo da carta.
     * Deve ser chamado após o escudo proteger contra um ataque.
     */
    public void removerEscudo() {
        this.temEscudo = false;
    }

    /**
     * Processa o dano recebido pela carta.
     * Se a carta tiver escudo, o dano é totalmente bloqueado e o escudo desaparece.
     * Se não tiver escudo, o dano é aplicado à vida.
     *
     * @param dano Quantidade de dano recebido
     * @return true se o escudo foi ativado e bloqueou o dano, false se o dano foi aplicado à vida
     */
    public boolean receberDano(int dano) {
        if (dano <= 0) {
            return false;
        }

        if (this.temEscudo) {
            this.removerEscudo();
            return true;
        }
        this.vida -= dano;
        return false;
    }

    /**
     * Aplica o único efeito da carta.
     * @param oponente Jogador oponente
     * @param proprietario Jogador dono da carta
     * @param fase Fase do jogo
     */
    public void aplicarEfeito(Jogador oponente, Jogador proprietario, String fase) {
        if (this.efeito != null) {
            this.efeito.executarEfeito(this, oponente, proprietario, fase);
        }
    }

    /**
     * Verifica se a carta tem um efeito definido.
     */
    public boolean temEfeito() {
        return this.efeito != null;
    }

    // Método utilitário para clonar a carta (útil para o jogo)
    public Carta clone() {
        Carta copia = new Carta(this.id, this.nome, this.imagem, this.descricao, this.elemento,
                this.poderDeLuta, this.vida, this.efeito);
        copia.setTemEscudo(this.temEscudo);
        return copia;
    }

    @Override
    public String toString() {
        String efeitoStr = temEfeito() ? efeito.getNomeEfeito() : "Sem efeito";
        String escudoStr = temEscudo ? "[🛡️]" : "";
        return String.format("%s[%s] %s (%s) | ATK: %d | HP: %d | %s",
                escudoStr, this.nome, this.elemento, this.poderDeLuta, this.vida, efeitoStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carta carta = (Carta) o;
        return Objects.equals(id, carta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}