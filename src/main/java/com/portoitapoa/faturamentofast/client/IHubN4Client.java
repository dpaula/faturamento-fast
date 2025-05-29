package com.portoitapoa.faturamentofast.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.portoitapoa.faturamentofast.vo.n4.vo.N4InputVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Alan Lopes
 */
@FeignClient(value = "hub-n4") //, url = "https://hub-n4-dev-k8s.portoitapoa.com")
public interface IHubN4Client {

    @PostMapping(value = "/api/processos-n4/hubn4", consumes = {MediaType.APPLICATION_JSON_VALUE})
    String executaGroovyN4(@Valid @RequestBody N4InputVO input) throws JsonProcessingException;

}
