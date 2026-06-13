package com.cardgame.logic;

import com.cardgame.model.Bot;

public class SessaoJogo {

    // Todos os atributos/métodos são estáticos, justamente para serem acessadas globalmente
    // Essa abordagem teria de ser removida caso usassemos testes unitários

    private static String nicknameAtual;
    private static boolean carregandoDeSave;
    private static Bot adversarioAtual;

    private SessaoJogo() {
    }

    public static String getNicknameAtual() {
        return nicknameAtual;
    }

    public static void setNicknameAtual(String nicknameAtual) {
        SessaoJogo.nicknameAtual = nicknameAtual;
    }

    public static boolean temNickname() {
        return nicknameAtual != null && !nicknameAtual.isBlank();
    }

    public static boolean isCarregandoDeSave() {
        return carregandoDeSave;
    }

    public static void setCarregandoDeSave(boolean carregandoDeSave) {
        SessaoJogo.carregandoDeSave = carregandoDeSave;
    }

    public static Bot getAdversarioAtual() {
        return adversarioAtual;
    }

    public static void definirAdversarioAtual(Bot adversarioAtual) {
        SessaoJogo.adversarioAtual = adversarioAtual;
    }

    public static boolean temAdversarioAtual() {
        return adversarioAtual != null;
    }

    public static void limpar() {
        nicknameAtual = null;
        carregandoDeSave = false;
        adversarioAtual = null;
    }
}