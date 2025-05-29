package com.portoitapoa.faturamentofast.client.impl;

import com.portoitapoa.faturamentofast.client.IManagerClient;
import com.portoitapoa.faturamentofast.enuns.EnManagerStatusType;
import com.portoitapoa.faturamentofast.util.DateUtil;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.dtos.ProcessManagerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ManagerClient {

    private final IManagerClient managerClient;
    private static final int DEFAULT_YEAR = 2024;

    /**
     * Busca um processo no gerenciador pelo BL informado.
     * Se o BL for nulo, retorna null.
     * Se o processo for encontrado, retorna o ProcessManagerDTO, caso contr rio, retorna null.
     * Se ocorrer um erro na busca, retorna null.
     *
     * @param bl N mero do BL
     * @return ProcessManagerDTO
     */
    public ProcessManagerDTO findFinishedProcessByBl(String bl) {
        if (bl == null) {
            log.warn("{} BL cannot be null", Util.LOG_PREFIX);
            return null;
        }

        try {
            LocalDate now = LocalDate.now();
            var process = managerClient.listProcess(EnManagerStatusType.FINISHED, bl,
                    DateUtil.formatLocalDateToString(now.withYear(DEFAULT_YEAR)),
                    DateUtil.formatLocalDateToString(now),
                    true
            );
            return process != null && !process.getObjectsList().isEmpty()
                    ? process.getObjectsList().get(0)
                    : null;
        } catch (Exception e) {
            log.error("{} Error searching for bl {}", Util.LOG_PREFIX, bl, e);
            return null;
        }
    }
}
