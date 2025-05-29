package com.portoitapoa.faturamentofast.enuns;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Fernando de Lima
 *
 */
public enum EnCategoriaCue {

    @JsonProperty("IMPRT")
    IMPORTACAO,

    @JsonProperty("EXPRT")
    EXPORTACAO
}
