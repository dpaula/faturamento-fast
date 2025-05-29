package com.portoitapoa.faturamentofast.client;

import com.portoitapoa.faturamentofast.vo.SolicitacaoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Dionízio Inácio
 */
@FeignClient(name = "importacao-ctac-cte-cabotagem")
public interface ICabotagemImpoClient {

    /**
     * Get para trazer uma SolicitacaoVO através de um BL
     */
    @GetMapping("/api/solicitacoes/filter")
    Page<SolicitacaoVO> filter(
        @RequestParam(value = "buscar", required = false) String buscar,
        @PageableDefault(sort = "criadoEm", direction = Sort.Direction.DESC, size = 20) Pageable pageable);
}
