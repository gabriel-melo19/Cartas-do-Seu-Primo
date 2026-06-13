package com.cardgame.effects;

import com.cardgame.model.TipoEfeito;
import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica para criar efeitos de cartas baseada no tipo.
 * Centraliza a criação de objetos e facilita extensões futuras.
 */

public class EfeitoFactory {

    private static final Map<TipoEfeito, Class<? extends EfeitoCarta>> MAPEAMENTO_EFEITOS = new HashMap<>();

    static {
        MAPEAMENTO_EFEITOS.put(TipoEfeito.ESCUDO_INICIAL, EfeitoEscudoInicial.class);
    }

    /**
     * Cria um efeito baseado no tipo e parâmetros.
     * @param tipo Tipo do efeito
     * @param parametros Parâmetros específicos do efeito
     * @return Instância do efeito criado
     */
    public static EfeitoCarta criarEfeito(TipoEfeito tipo, Map<String, Object> parametros) {
        Class<? extends EfeitoCarta> classeEfeito = MAPEAMENTO_EFEITOS.get(tipo);

        if (classeEfeito == null) {
            throw new IllegalArgumentException("Tipo de efeito desconhecido: " + tipo);
        }

        try {
            return classeEfeito.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar efeito: " + tipo, e);
        }
    }
}
