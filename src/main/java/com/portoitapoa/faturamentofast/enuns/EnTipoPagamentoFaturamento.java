package com.portoitapoa.faturamentofast.enuns;

import lombok.Getter;

@Getter
public enum EnTipoPagamentoFaturamento {

    A_VISTA("A Vista"),
    A_PRAZO("A Prazo");

    private final String tipo;

    EnTipoPagamentoFaturamento(final String tipo) {
        this.tipo = tipo;
    }
}