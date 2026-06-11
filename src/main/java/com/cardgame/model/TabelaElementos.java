package com.cardgame.model;

public class TabelaElementos {

    public static double calcularMultiplicador(Elemento atacante, Elemento defensor) {

        if (atacante == null || defensor == null) {
            return 1.0;
        }

        switch (atacante) {

            case FOGO:
                if (defensor == Elemento.PLANTA || defensor == Elemento.GELO) return 2.0;
                if (defensor == Elemento.AGUA || defensor == Elemento.TERRA) return 0.5;
                break;

            case AGUA:
                if (defensor == Elemento.FOGO || defensor == Elemento.TERRA) return 2.0;
                if (defensor == Elemento.PLANTA || defensor == Elemento.ELETRICO) return 0.5;
                break;

            case PLANTA:
                if (defensor == Elemento.AGUA || defensor == Elemento.LUZ) return 2.0;
                if (defensor == Elemento.FOGO || defensor == Elemento.GELO) return 0.5;
                break;

            case TERRA:
                if (defensor == Elemento.FOGO || defensor == Elemento.ELETRICO) return 2.0;
                if (defensor == Elemento.METAL || defensor == Elemento.AGUA) return 0.5;
                break;

            case ELETRICO:
                if (defensor == Elemento.METAL || defensor == Elemento.AGUA) return 2.0;
                if (defensor == Elemento.LUZ || defensor == Elemento.TERRA) return 0.5;
                break;

            case GELO:
                if (defensor == Elemento.TERRA || defensor == Elemento.PLANTA) return 2.0;
                if (defensor == Elemento.FOGO || defensor == Elemento.METAL) return 0.5;
                break;

            case METAL:
                if (defensor == Elemento.TERRA || defensor == Elemento.GELO) return 2.0;
                if (defensor == Elemento.ELETRICO || defensor == Elemento.TREVAS) return 0.5;
                break;

            case LUZ:
                if (defensor == Elemento.ELETRICO || defensor == Elemento.TREVAS) return 2.0;
                if (defensor == Elemento.PLANTA) return 0.5; // corrigido
                break;

            case TREVAS:
                if (defensor == Elemento.METAL) return 2.0;
                if (defensor == Elemento.LUZ) return 0.5;
                break;
        }

        return 1.0; // neutro
    }

    //verifica vantagem
    public static boolean ehSuperEfetivo(Elemento atacante, Elemento defensor) {
        return calcularMultiplicador(atacante, defensor) > 1.0;
    }

    //verifica desvantagem
    public static boolean ehPoucoEfetivo(Elemento atacante, Elemento defensor) {
        return calcularMultiplicador(atacante, defensor) < 1.0;
    }

    // mensagem pronta
    public static String getMensagemCombate(Elemento atacante, Elemento defensor) {
        double mult = calcularMultiplicador(atacante, defensor);

        if (mult > 1.0) {
            return "SUPER EFETIVO!";
        } else if (mult < 1.0) {
            return "POUCO EFETIVO...";
        }

        return "EFETIVO";
    }
}