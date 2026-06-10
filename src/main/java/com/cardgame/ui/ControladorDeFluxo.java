package com.cardgame.ui;

/**
 * Contrato que todos os controllers de tela devem implementar.
 * Permite que o ScreenManager injete a si mesmo em qualquer
 * controller após o FXMLLoader criar a tela.
 */
public interface ControladorDeFluxo {

    void configurar(ScreenManager screenManager);
}