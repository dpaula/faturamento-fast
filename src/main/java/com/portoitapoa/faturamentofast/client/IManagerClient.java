package com.portoitapoa.faturamentofast.client;

import com.portoitapoa.faturamentofast.enuns.EnManagerStatusType;
import com.portoitapoa.faturamentofast.dtos.PagedObjectsDTO;
import com.portoitapoa.faturamentofast.dtos.ProcessManagerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "pri-manager", url = "https://api-dev.portoitapoa.com/pri-manager")
public interface IManagerClient {


    /**
     * Recupera todos processos com status e/ou numero de BL especificados.
     *
     * @param status status do processo
     * @param term   termo de pesquisa
     * @return lista de processos
     */
    @GetMapping(value = "/process", consumes = MediaType.APPLICATION_JSON_VALUE)
    PagedObjectsDTO<ProcessManagerDTO> listProcess(@RequestParam(required = false) EnManagerStatusType status,
                                                   @RequestParam(required = false, value = "ft_term") String term,
                                                   @RequestParam(required = false, value = "start_date") String startDate,
                                                   @RequestParam(required = false, value = "end_date") String endDate,
                                                   @RequestParam(required = false) boolean internal);
}
