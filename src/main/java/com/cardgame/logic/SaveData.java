package com.cardgame.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SaveData {

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("deckIdSelecionado")
    private String deckIdSelecionado;

    @JsonProperty("cartasDoDeckSelecionado")
    private List<String> cartasDoDeckSelecionado;

    @JsonProperty("botsDerrotados")
    private List<String> botsDerrotados;

    @JsonProperty("ultimoBotEnfrentado")
    private String ultimoBotEnfrentado;

    public SaveData() {
        this.cartasDoDeckSelecionado = new ArrayList<>();
        this.botsDerrotados = new ArrayList<>();
        this.ultimoBotEnfrentado = "";
    }

    public SaveData(String nickname, String deckIdSelecionado) {
        this.nickname = nickname;
        this.deckIdSelecionado = deckIdSelecionado;
        this.cartasDoDeckSelecionado = new ArrayList<>();
        this.botsDerrotados = new ArrayList<>();
        this.ultimoBotEnfrentado = "";
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDeckIdSelecionado() {
        return deckIdSelecionado;
    }

    public void setDeckIdSelecionado(String deckIdSelecionado) {
        this.deckIdSelecionado = deckIdSelecionado;
    }

    public List<String> getCartasDoDeckSelecionado() {
        return cartasDoDeckSelecionado;
    }

    public void setCartasDoDeckSelecionado(List<String> cartasDoDeckSelecionado) {
        this.cartasDoDeckSelecionado = cartasDoDeckSelecionado != null
                ? new ArrayList<>(cartasDoDeckSelecionado)
                : new ArrayList<>();
    }

    public List<String> getBotsDerrotados() {
        return botsDerrotados;
    }

    public void setUltimoBotEnfrentado(String ultimoBotEnfrentado) {
        this.ultimoBotEnfrentado = ultimoBotEnfrentado;
    }

    public void marcarBotComoDerrotado(String botId) {
        if (botId == null || botId.isBlank()) {
            return;
        }

        if (!botsDerrotados.contains(botId)) {
            botsDerrotados.add(botId);
        }
    }

}