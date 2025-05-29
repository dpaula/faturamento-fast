package com.portoitapoa.faturamentofast.client.impl;

import com.portoitapoa.faturamentofast.client.IEmailClient;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.EmailInputSimples;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailClient {

    private final IEmailClient client;

    @Async
    public void enviarSimples(final EmailInputSimples email) {
        log.info("{} Chamando serviço para enviar email, titulo: {}, destinatarios: {}", Util.LOG_PREFIX,
            email.getTitulo(),
            email.getDestinatarios());

        try {
            client.enviarSimples(email);
        } catch (final Exception e) {
            log.error("{} Erro ao tentar enviar email {}", Util.LOG_PREFIX, email,
                e);
        }
    }

    @Async
    public void enviarSimples(final HttpHeaders headers, final EmailInputSimples email) {
        log.info("{} Chamando serviço para enviar email, titulo: {}, destinatarios: {}", Util.LOG_PREFIX,
                email.getTitulo(),
                email.getDestinatarios());

        try {
            client.enviarSimples(headers, email);
        } catch (final Exception e) {
            log.error("{} Erro ao tentar enviar email {}", Util.LOG_PREFIX, email,
                    e);
        }
    }

}
