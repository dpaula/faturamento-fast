package com.portoitapoa.faturamentofast.client;

import com.portoitapoa.faturamentofast.model.Container;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Fernando de Lima
 * <p>
 * Interface que define a conexão para o microsserviço de faturamento-n4
 */
@FeignClient(value = "faturamento-n4") //, url = "https://faturamento-n4-staging-k8s.portoitapoa.com")
public interface IFaturamentoN4Client {

    @GetMapping("/api/processos-n4/cues-expo")
    List<Container> cuesProcessosN4Expo(
        @RequestParam(required = false) String navio,
        @RequestParam(required = false) String booking,
        @RequestParam(required = false) boolean criarExcecaoPortal);
}
