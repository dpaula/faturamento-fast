package com.portoitapoa.faturamentofast.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.portoitapoa.faturamentofast.client.IHubN4Client;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.n4.vo.N4InputVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * @author Alan Lopes on 20/07/2021
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HubN4Client {

    private final IHubN4Client client;

    public String executaGroovyN4(@Valid final N4InputVO input) throws JsonProcessingException {
        log.info("{} Chamando serviço para integrar com o N4.", Util.LOG_PREFIX);
        return client.executaGroovyN4(input);
    }
}