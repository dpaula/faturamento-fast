package com.portoitapoa.faturamentofast.client;

import com.portoitapoa.faturamentofast.vo.EmailInputSimples;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author Fernando de Lima
 */
@FeignClient(name = "email", url = "https://email-prod-k8s.portoitapoa.com")
public interface IEmailClient {

    /**
     * Post para Envia e-mail breve e simples
     */
    @PostMapping("/api/emails/enviar-simples")
    void enviarSimples(@RequestBody EmailInputSimples email);

    @PostMapping("/api/emails/enviar-simples")
    void enviarSimples(@RequestHeader HttpHeaders headers, @RequestBody EmailInputSimples email);
}
