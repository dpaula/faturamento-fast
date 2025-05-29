package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.enuns.EnTagException;
import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * @author Fernando de Lima on 25/08/20
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CockPitService {

    public static final String SUCCESS = "success";
    public static final String TYPE = "type";
    public static final String MESSAGE = "messsage";

    @Value("${app.url-cokpit.servico-evento-excecao}")
        private String url;

    public void enviarEventoExcecao(final List<Long> gkeys) {
        log.info("{} Enviando faturamento para cadastrar eventos para exceção com cockpit, eventos: {}", Util.LOG_PREFIX, gkeys);

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final var request = new HttpEntity<>(gkeys.toString(), headers);

        final var object = postEventoExcecao(request);

        validarRetornoCockPit(gkeys, object);
    }

    private Object postEventoExcecao(final HttpEntity<String> request) {
        log.info("{} Iniciando o post de evento excecao, request body: {}", Util.LOG_PREFIX, request.getBody());
        try {
            return new RestTemplate().postForObject(url, request, Object.class);
        } catch (final Exception e) {
            log.error("{} Erro ao chamar serviço do cokpit para eventos: Request: {}, Erro: {}", Util.LOG_PREFIX, request, e.getMessage());
            throw new WSFaturamentoFastException(EnTagException.COMUNICATION_ERROR_COCKPIT);
        }
    }

    private void validarRetornoCockPit(final List<Long> gkeys, final Object object) {
        if(!(object instanceof LinkedHashMap)){
            log.error("{} Erro ao chamar serviço do cokpit para eventos: {}", Util.LOG_PREFIX, gkeys);
            throw new WSFaturamentoFastException(EnTagException.COMUNICATION_ERROR_COCKPIT);
        }

        final var mapa = (LinkedHashMap) object;
        final var typeRetornoServico = String.valueOf(mapa.get(TYPE));
        final var messageRetornoServico = String.valueOf(mapa.get(MESSAGE));

        if(!SUCCESS.equalsIgnoreCase(typeRetornoServico)){
            log.error("{} Erro ao chamar serviço do cokpit para eventos: {}, mensagem: {}", Util.LOG_PREFIX, gkeys, messageRetornoServico);
            throw new WSFaturamentoFastException(EnTagException.COMUNICATION_ERROR_COCKPIT);
        }

        log.info("{} "+messageRetornoServico+": {}", Util.LOG_PREFIX, gkeys);
    }
}
