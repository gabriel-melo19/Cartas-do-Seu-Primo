package com.cardgame.model;

/**
 * Enum define os resultados possíveis ao tentar jogar uma carta no tabuleiro.
 */
public enum JogadaResultado {
    SUCESSO("Carta colocada no tabuleiro."),
    TROCA_REALIZADA("Troca efetuada! A carta antiga voltou para sua mão. Você já não pode trocar mais neste turno."),
    TROCA_JA_USADA("Você já realizou a troca deste turno. Aguarde o próximo turno para substituir outra carta."),
    ERRO_INDICE_INVALIDO("Erro: Seleção inválida.");

    private final String mensagem;

    JogadaResultado(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
