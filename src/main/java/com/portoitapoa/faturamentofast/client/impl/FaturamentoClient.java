package com.portoitapoa.faturamentofast.client.impl;

import com.portoitapoa.faturamentofast.client.IFaturamentoClient;
import com.portoitapoa.faturamentofast.entity.Siscomex;
import com.portoitapoa.faturamentofast.enuns.EnTagException;
import com.portoitapoa.faturamentofast.enuns.EnTagException;
import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import com.portoitapoa.faturamentofast.model.FaturamentoExportacao;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.FaturamentoExportacaoVO;
import com.portoitapoa.faturamentofast.vo.FaturamentoSiscomexVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaturamentoClient {

    private final IFaturamentoClient client;

    /**
     * POST do um novo processo de Faturamento Online
     */
    public void postFaturamentoPrimeiraParteSiscomex(final Siscomex siscomex, final boolean isRealUser) {

        final var faturamentoOnline = FaturamentoSiscomexVO.parse(siscomex);
        log.info("{} Novo post de faturamento siscomex para Faturamento Online: {}", Util.LOG_PREFIX,
            siscomex.getBlOriginal());

        if (StringUtils.isBlank(faturamentoOnline.getCliente().getCnpj())) {
            log.error("{} Não encontrado CNPJ (cnpjFaturarPara): {}  para o processo!",Util.LOG_PREFIX,
                    faturamentoOnline.getCliente().getCnpj());
            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CNPJ_FATURAR_PARA);
        }

        try {
            client.postFaturamentoPrimeiraParteSiscomex(faturamentoOnline, isRealUser);
        } catch (final Exception e) {
            log.error("{} Erro ao tentar enviar faturamento siscomex para Faturamento Online {}. Error: {}",
                    Util.LOG_PREFIX, faturamentoOnline, e.getMessage());
            throw new WSFaturamentoFastException(EnTagException.COMUNICATION_ERROR_FAT);
        }
        log.info("{} Sucesso no envio de novo faturamento siscomex para Faturamento Online, bl: {}", Util.LOG_PREFIX,
            faturamentoOnline.getNumeroBl());
    }

    public void postFaturamentoOnlineExpo(final FaturamentoExportacao faturamentoExpo, final boolean isRealUser) {

        log.info(
            "{} Novo post de faturamento de exportacao para Faturamento Online, cnpjExportador: {}, navio: {}, bookings: {}",
            Util.LOG_PREFIX,
            faturamentoExpo.getCliente().getCnpj(), faturamentoExpo.getNavio(), faturamentoExpo.getBookings());

        if (StringUtils.isBlank(faturamentoExpo.getCliente().getCnpj())) {
            log.error("{} Não encontrado CNPJ do exportador: {}  para o processo!",Util.LOG_PREFIX,
                    faturamentoExpo.getCliente().getCnpj());
            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CNPJ_EXPORTADOR);
        }

        try {
            client.postFaturamentoOnlineExpo(FaturamentoExportacaoVO.parse(faturamentoExpo), isRealUser);
        } catch (final Exception e) {
            log.error("{} Erro ao tentar enviar faturamento de exportacao para serviço de Faturamento Online {}",
                    Util.LOG_PREFIX, faturamentoExpo);
            throw new WSFaturamentoFastException(EnTagException.COMUNICATION_ERROR_FAT);
        }
        log.info(
            "{} Sucesso no envio de novo faturamento de exportacao para Faturamento Online, cnpjExportador: {}, navio: {}, bookings: {}",
            Util.LOG_PREFIX,
            faturamentoExpo.getCliente().getCnpj(), faturamentoExpo.getNavio(), faturamentoExpo.getBookings());
    }
}
