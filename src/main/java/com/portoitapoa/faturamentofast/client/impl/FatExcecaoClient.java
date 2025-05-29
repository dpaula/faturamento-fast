package com.portoitapoa.faturamentofast.client.impl;

import com.portoitapoa.faturamentofast.client.IFatExcecaoClient;
import com.portoitapoa.faturamentofast.enuns.EnCategoriaFaturamento;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FatExcecaoClient {

    private final IFatExcecaoClient client;

    /**
     * Post para consultar se existe Exceção de criar Faturamento para CNPJ
     */
    public boolean getExisteExcecao(final String cnpj, final EnCategoriaFaturamento invoicingType) {
        log.info("{} Verificando se existe exceção para o CNPJ: {}", Util.LOG_PREFIX, cnpj);

        try {
            return client.getExisteExcecao(cnpj, invoicingType);
        } catch (final Exception e) {
            log.error("{} Erro ao chamar serviço fat-excecao para verificar cnpj: {}, erro: {}", Util.LOG_PREFIX, cnpj,
                e.getMessage());
            return true;
        }
    }

    public List<String> getExcecoesBooking(final String cnpj) {
        log.info("{} Buscando excecoes de eventos Booking para o CNPJ: {}", Util.LOG_PREFIX, cnpj);

        try {
            return client.getExcecoesBooking(cnpj);
        } catch (final Exception e) {
            log.error(
                "{} Erro ao chamar serviço fat-excecao para listar eventos de excecao de bookings, cnpj: {}, erro: {}",
                Util.LOG_PREFIX, cnpj,
                e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<String> getExcecoesBl(final String cnpj) {
        log.info("{} Buscando excecoes de eventos Bl para o CNPJ: {}", Util.LOG_PREFIX, cnpj);

        try {
            return client.getExcecoesBl(cnpj);
        } catch (final Exception e) {
            log.error(
                    "{} Erro ao chamar serviço fat-excecao para listar eventos de excecao de bls, cnpj: {}, erro: {}",
                    Util.LOG_PREFIX, cnpj,
                    e.getMessage());
            return new ArrayList<>();
        }
    }
}
