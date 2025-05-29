package com.portoitapoa.faturamentofast.client.impl;

import com.portoitapoa.faturamentofast.client.IFaturamentoN4Client;
import com.portoitapoa.faturamentofast.model.Container;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaturamentoN4Client {

    private final IFaturamentoN4Client client;

    public List<Container> cuesProcessosN4Expo(final String booking, final String navio,
        final boolean criarExcecaoPortal) {
        log.info("{} Buscando CUEs no N4 - booking: {}, Navio: {}", Util.LOG_PREFIX, booking, navio);
        try {
            return client.cuesProcessosN4Expo(navio, booking, criarExcecaoPortal);
        } catch (final Exception e) {
            log.error(
                "{} Retorno fallback ao tentar carregar os Eventos Cues de exportacao, Navio: {}, Booking: {}, erro: {}",
                Util.LOG_PREFIX, navio, booking, e);
            return List.of();
        }
    }

}

