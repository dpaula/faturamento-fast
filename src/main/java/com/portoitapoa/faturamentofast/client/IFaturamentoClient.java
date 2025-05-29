package com.portoitapoa.faturamentofast.client;

import com.portoitapoa.faturamentofast.vo.FaturamentoExportacaoVO;
import com.portoitapoa.faturamentofast.vo.FaturamentoSiscomexVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Fernando de Lima
 * <p>
 * Interface que define a conexão para o microsserviço de faturamento
 */
@FeignClient(value = "faturamento") //, url = "https://faturamento-dev-k8s.portoitapoa.com")
public interface IFaturamentoClient {

    /**
     * POST do um novo processo de Faturamento Online
     */
    @PostMapping("/api/faturamentos/{isRealUser}")
    void postFaturamentoPrimeiraParteSiscomex(@Valid @RequestBody FaturamentoSiscomexVO faturamentoVO,
                                              @PathVariable boolean isRealUser);

    @PostMapping("/api/faturamentos/{isRealUser}")
    void postFaturamentoOnlineExpo(@RequestBody FaturamentoExportacaoVO faturamentoExpo,
                                   @PathVariable boolean isRealUser);
}