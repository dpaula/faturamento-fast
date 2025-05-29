package com.portoitapoa.faturamentofast.enuns;

import lombok.Getter;

/**
 * @author Gabriel Negrão on 24/10/2022
 */
@Getter
public enum EnFaturamentoFastException {
    COMUNICATION_ERROR_FAT("Erro de comunicação com o serviço de faturamento!"),
    COMUNICATION_ERROR_COCKPIT("Erro de comunicação com o serviço de cockpit!"),
    COMUNICATION_ERROR_FAT_EXC("Erro de comunicação com o serviço de faturamento de exceção!"),
    COMUNICATION_ERROR_N4("Erro de comunicação com o serviço do N4!"),
    COMUNICATION_ERROR_EMAIL("Erro de comunicação com o serviço de email!"),

    FAT_TYPE_PROC_INVALID_FAT_ONLINE("Faturamento com tipo de processo inválido para o Faturamento Online! Tipos inválidos: DI - LCL, Declaração Transito Aduaneiro - LCL, DTA - LCL"),
    FAT_CROSSDOCKING_WITHOUT_EVENT_OR_MANUAL("Faturamento possui crossdocking e não possui evento  CROSSDOCKING_MECANIZADO OU MANUAL!"),

    NOT_FOUND_SISCOMEX_FAT_BL("Não encontrado faturamento siscomex para esse BL!"),

    NOT_FOUND_SISCOMEX_FAT_ID("Faturamento Siscomex não encontrado com esse id!"),
    NOT_FOUND_PROCESS_EXPO("Não encontrado processo para esse cnpjExportador, Navio e Booking!"),
    NOT_FOUND_CNPJ("Não encontrado CNPJ para o processo!"),
    NOT_FOUND_CUES_BL("Não retornado Eventos CUES do N4 para o BL!"),
    NOT_FOUND_CUES_BOOKING("Não retornado Eventos CUES do N4 para os bookings!"),

    EXCEPTION_CLIENT_CNPJ("Cliente com exceção para criar Faturamento Online!"),
    EXCEPTION_BOOKING_BL("Cliente com exceção para criar Faturamento Online!"),
    NOT_FOUND_CNPJ_FATURAR_PARA("Não encontrado CNPJ (cnpjFaturarPara) para o processo!"),
    NOT_FOUND_CNPJ_EXPORTADOR("Não encontrado CNPJ do exportador para o processo!");

    private final String descricao;

    EnFaturamentoFastException(final String descricao) {
        this.descricao = descricao;
    }
}
