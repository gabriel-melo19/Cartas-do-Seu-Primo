package com.cardgame.model;

import java.util.Map;
/**
 * Enumeração dos elementos do card game.
 * Cada elemento possui suas relações de força definidas estaticamente.
 */
public enum Elemento {
    FOGO("Fogo", Map.of(
            "fortes_contra", new String[]{"PLANTA", "GELO"},
            "fracos_contra", new String[]{"AGUA", "TERRA"}
    )),

    AGUA("Agua", Map.of(
            "fortes_contra", new String[]{"FOGO", "TERRA"},
            "fracos_contra", new String[]{"PLANTA", "ELETRICO"}
    )),

    PLANTA("Planta", Map.of(
            "fortes_contra", new String[]{"AGUA", "LUZ"},
            "fracos_contra", new String[]{"FOGO", "GELO"}
    )),

    TERRA("Terra", Map.of(
            "fortes_contra", new String[]{"FOGO", "ELETRICO"},
            "fracos_contra", new String[]{"METAL", "AGUA"}
    )),

    ELETRICO("Eletrico", Map.of(
            "fortes_contra", new String[]{"METAL", "AGUA"},
            "fracos_contra", new String[]{"LUZ", "TERRA"}
    )),

    GELO("Gelo", Map.of(
            "fortes_contra", new String[]{"TERRA", "PLANTA"},
            "fracos_contra", new String[]{"FOGO", "METAL"}
    )),

    METAL("Metal", Map.of(
            "fortes_contra", new String[]{"TERRA", "GELO"},
            "fracos_contra", new String[]{"ELETRICO", "TREVAS"}
    )),

    LUZ("Luz", Map.of(
            "fortes_contra", new String[]{"ELETRICO", "TREVAS"},
            "fracos_contra", new String[]{"PLANTA", "TREVAS"}
    )),

    TREVAS("Trevas", Map.of(
            "fortes_contra", new String[]{"METAL"},
            "fracos_contra", new String[]{"LUZ"}
    ));

    private final String nomeExibicao;
    private final Map<String, String[]> relacoes;

    Elemento(String nomeExibicao, Map<String, String[]> relacoes) {
        this.nomeExibicao = nomeExibicao;
        this.relacoes = relacoes;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    /**
     * Verifica se este elemento é forte contra o alvo.
     *
     * @param alvo Elemento que será atacado
     * @return true se houver vantagem elemental
     */
    public boolean ehForteContra(Elemento alvo) {
        String[] fortes = relacoes.get("fortes_contra");
        if (fortes == null) return false;

        for (String elemento : fortes) {
            if (elemento.equals(alvo.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se este elemento é fraco contra o alvo.
     *
     * @param alvo Elemento atacante
     * @return true se estiver em desvantagem elemental
     */
    public boolean ehFracoContra(Elemento alvo) {
        String[] fracas = relacoes.get("fracos_contra");
        if (fracas == null) return false;

        for (String elemento : fracas) {
            if (elemento.equals(alvo.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna o multiplicador de ataque baseado na interação com outro elemento.
     *
     * @param alvo Elemento que receberá o dano
     * @return Multiplicador (1.5 = super efetivo, 0.5 = pouco efetivo, 1.0 = neutro)
     */
    public double getMultiplicadorAtaqueContra(Elemento alvo) {
        if (this.ehForteContra(alvo)) {
            return 1.5;
        } else if (this.ehFracoContra(alvo)) {
            return 0.5;
        }
        return 1.0;
    }

    /**
     * Retorna o multiplicador de vida quando confrontado com outro elemento.
     * Regra do projeto: elemento forte também ganha bonus defensivo.
     *
     * @param adversario Elemento no tabuleiro oposto
     * @return Multiplicador de vida
     */
    public double getMultiplicadorVidaQuandoConfrontado(Elemento adversario) {
        if (this.ehForteContra(adversario)) {
            return 1.3; // Bônus defensivo moderado
        } else if (adversario.ehForteContra(this)) {
            return 0.8; // Desvantagem defensiva
        }
        return 1.0;
    }
}