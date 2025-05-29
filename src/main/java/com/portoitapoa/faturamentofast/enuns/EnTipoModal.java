package com.portoitapoa.faturamentofast.enuns;

import lombok.Getter;

/**
 * @author Dionízio Inácio
 */
@Getter
public enum EnTipoModal  {

    PORTA_PORTA(1, "PORTA_PORTA"),
    PORTA_PORTO(2, "PORTA_PORTO"),
    PORTO_PORTA(3, "PORTO_PORTA"),
    PORTO_PORTO(4, "PORTO_PORTO");

    private final Integer id;
    private final String descricao;

    EnTipoModal(final Integer id, final String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public static EnTipoModal convert(String xCaracAd) {
        switch (xCaracAd) {
            case "PA/PA" -> {
                return PORTA_PORTA;
            }
            case "PA/PO" -> {
                return PORTA_PORTO;
            }
            case "PO/PA" -> {
                return PORTO_PORTA;
            }
            case "PO/PO" -> {
                return PORTO_PORTO;
            }
            default -> {
                return PORTA_PORTA;
            }
        }
    }
}
