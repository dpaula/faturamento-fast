package com.portoitapoa.faturamentofast.enuns;

import lombok.Getter;

/**
 *
 * @author Fernando de Lima
 *
 */
@Getter
public enum EnCategoriaFaturamento {

    IMPORTACAO(1, "Importação"),
    EXPORTACAO(2, "Exportação"),
    EXPORTACAO_CABOTAGEM(3, "Exportação-Cabot"),
    IMPORTACAO_CABOTAGEM(4, "Importação-Cabot");

    private final Integer id;
    private final String tipo;

    EnCategoriaFaturamento(final Integer id, final String tipo) {
        this.id = id;
        this.tipo = tipo;
    }
}
