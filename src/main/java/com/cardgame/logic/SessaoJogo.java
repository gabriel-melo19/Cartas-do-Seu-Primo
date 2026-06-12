package com.cardgame.logic;

public class SessaoJogo {

    private static String nicknameAtual;
    private static boolean carregandoDeSave;

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

    public static void limpar() {
        nicknameAtual = null;
        carregandoDeSave = false;
    }
}