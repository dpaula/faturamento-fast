package com.portoitapoa.faturamentofast.client.impl;

import com.portoitapoa.faturamentofast.client.ICabotagemImpoClient;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.SolicitacaoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CabotagemImpoClient {

    private final ICabotagemImpoClient client;

    public SolicitacaoVO filter(
        String buscar,
        Pageable pageable) {
        log.info("{} Chamando serviço para pesquisar uma SolicitacaoVO (Caobotagem Impo): {}", Util.LOG_PREFIX, buscar);

        SolicitacaoVO s = null;
        try {
            var retorno = client.filter(
                buscar,
                pageable);

            if (retorno.getContent().size() > 0) {
                List<SolicitacaoVO> lst = retorno.getContent();
                s = lst.get(0);
            }
            return s;
        } catch (final Exception e) {
            log.error("{} Erro ao pesquisar o bl {}", Util.LOG_PREFIX, buscar, e);
        }
        return null;
    }

}
