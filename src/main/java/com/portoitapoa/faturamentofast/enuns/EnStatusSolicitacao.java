package com.portoitapoa.faturamentofast.enuns;

import lombok.Getter;

/**
 * @author Alisson Lopes
 */
@Getter
public enum EnStatusSolicitacao  {

    RASCUNHO(1, "RASCUNHO"),
    AGUARDANDO_LIBERACAO(2, "AGUARDANDO_LIBERACAO"),
    ANALISANDO(3, "ANALISANDO"),
    CANCELADA(4, "CANCELADA"),
    LIBERADA(5, "LIBERADA");

    private final Integer id;
    private final String descricao;

    EnStatusSolicitacao(final Integer id, final String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

}
