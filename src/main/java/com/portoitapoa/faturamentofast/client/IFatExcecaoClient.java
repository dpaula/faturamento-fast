package com.portoitapoa.faturamentofast.client;

import com.portoitapoa.faturamentofast.enuns.EnCategoriaFaturamento;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Fernando de Lima
 */
@FeignClient(value = "fat-excecao") //, url = "https://fat-excecao-dev-k8s.portoitapoa.com")
public interface IFatExcecaoClient {

    /**
     * Post para consultar se existe Exceção de criar Faturamento para CNPJ
     */
    @GetMapping("/api/excecoes/existe-excecao")
    boolean getExisteExcecao(@RequestParam String cnpj, @RequestParam EnCategoriaFaturamento invoiceType);

    /**
     * Retorna lista de Bookings com Exceção para CNPJ
     */
    @GetMapping("/api/excecoes/eventos/excecoes-booking")
    List<String> getExcecoesBooking(@RequestParam(value = "cnpj") String cnpj);

    /**
     * Retorna lista de Bl com Exceção para CNPJ
     */
    @GetMapping("/api/excecoes/eventos/excecoes-bl")
    List<String> getExcecoesBl(@RequestParam(value = "cnpj") String cnpj);
}
