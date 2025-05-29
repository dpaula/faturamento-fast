package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.client.impl.FatExcecaoClient;
import com.portoitapoa.faturamentofast.dtos.FaturamentoInputDTO;
import com.portoitapoa.faturamentofast.dtos.ProcessManagerDTO;
import com.portoitapoa.faturamentofast.entity.Siscomex;
import com.portoitapoa.faturamentofast.enuns.EnCategoriaFaturamento;
import com.portoitapoa.faturamentofast.enuns.EnTagException;
import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import com.portoitapoa.faturamentofast.kafka.payload.PreRegisterImpoPayload;
import com.portoitapoa.faturamentofast.kafka.producer.PreRegisterImportProducer;
import com.portoitapoa.faturamentofast.util.Constants;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.EmailInputSimples;
import com.portoitapoa.faturamentofast.vo.EventoVO;
import com.portoitapoa.faturamentofast.vo.FaturamentoSiscomexVO;
import com.portoitapoa.faturamentofast.vo.InformacoesBLVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaturamentoProcessImportService {

    private final N4Service n4Service;
    private final CockPitService cockPitService;
    private final EmailService emailService;
    private final PreRegisterImportProducer preRegisterImportProducer;
    private final FatExcecaoClient fatExcecaoClient;

    /**
     * Realiza o processamento de faturamento de um Siscomex.
     *
     * @param siscomex dados do Siscomex
     * @param isRealUser flag para indicar se e um usu rio real ou um job
     *
     * @return dados do faturamento
     */
    public FaturamentoSiscomexVO siscomexImport(final Siscomex siscomex, final boolean isRealUser) {
        return processBilling(FaturamentoInputDTO.fromSiscomex(siscomex), isRealUser, false);
    }

    /**
     * Realiza o processamento de faturamento de um processo de importa o do Manager.
     *
     * @param dto objeto contendo os dados do processo Manager
     * @param isRealUser flag para indicar se e um usu rio real ou um job
     *
     * @return dados do faturamento
     */
    public FaturamentoSiscomexVO processManagerImport(final ProcessManagerDTO dto, final boolean isRealUser) {
        return processBilling(FaturamentoInputDTO.fromManagerDTO(dto), isRealUser, true);
    }

    private FaturamentoSiscomexVO processBilling(final FaturamentoInputDTO input, final boolean isRealUser, boolean isManager) {
        validateInput(input);

        setDefaultValues(input);

        String di = input.getTipo().equals("DTC") ? "" : input.getDi();
        List<InformacoesBLVO> containers = fetchContainers(input, isManager, di);

        validateCrossDocking(input, containers);

        input.setNavio(buscarNavioCues(containers));
        List<Long> gkeys = buscarGkeysServicosCues(containers);
        cockPitService.enviarEventoExcecao(gkeys);

        final var faturamentoOnline = FaturamentoSiscomexVO.parse(input);
        validateFaturamento(faturamentoOnline);

        preRegisterImportProducer.sendMessage(new PreRegisterImpoPayload(isRealUser, faturamentoOnline));
        input.setGkeyServicosExcecaoCockPit(gkeys);

        return faturamentoOnline;
    }

    private void validateInput(FaturamentoInputDTO input) {
        if (StringUtils.isBlank(input.getCnpjFaturarPara())) {
            log.error("{} Não encontrado [CNPJ Faturar Para] para prosseguir com o Faturamento!", Util.LOG_PREFIX);
            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CNPJ_FATURAR_PARA);
        }
        faturamentoImpoComExcecao(input.getCnpjFaturarPara(), input.getBlOriginal());
    }

    private void setDefaultValues(FaturamentoInputDTO input) {
        if ("DTC".equals(input.getTipo())) {
            if (StringUtils.isBlank(input.getCnpjAdquirente())) {
                input.setCnpjAdquirente(input.getCnpjFaturarPara());
                input.setNomeAdquirente(input.getNomeFaturarPara());
            }
            input.setDataFaturamento(input.getDataLiberacao().toLocalDate());
        }
    }

    private List<InformacoesBLVO> fetchContainers(FaturamentoInputDTO input, boolean isManager, String di) {
        List<InformacoesBLVO> containers = isManager
                ? n4Service.buscarInformacoesBLPorCE(input.getCe(), input.getBlOriginal())
                : n4Service.buscarInformacoesBLPorDI(di, input.getBlOriginal());

        if (containers.isEmpty()) {
            log.error("{} Não retornado Eventos CUES para o BL [{}]", Util.LOG_PREFIX, input.getBlOriginal());
            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CUES_BL);
        }
        return containers;
    }

    private void validateCrossDocking(FaturamentoInputDTO input, List<InformacoesBLVO> containers) {
        if (Boolean.TRUE.equals(input.getCrossdocking())) {
            List<String> cues = containers.stream()
                    .flatMap(container -> container.getChargeableUnitEvent().stream())
                    .map(EventoVO::getBexuEventType)
                    .collect(Collectors.toList());

            log.info("{} Eventos retornados: {}", Util.LOG_PREFIX, String.join(", ", cues));

            if (!Util.containAnyInList(cues, Constants.EVENTS_CROSSDOCKING)) {
                enviarEmailCrossDocking(input);
                throw new WSFaturamentoFastException(EnTagException.FAT_CROSSDOCKING_WITHOUT_EVENT_OR_MANUAL);
            }
        }
    }

    private void enviarEmailCrossDocking(FaturamentoInputDTO input) {
        var email = EmailInputSimples.builder()
                .titulo(Util.LOG_PREFIX + " - Faturamento possui crossdocking e não possui evento CROSSDOCKING!")
                .corpo(getCorpo(input))
                .build();
        emailService.enviar(email);
        log.error("{} Faturamento possui crossdocking e não possui evento CROSSDOCKING!", Util.LOG_PREFIX);
    }

    private void validateFaturamento(FaturamentoSiscomexVO faturamentoOnline) {
        if (StringUtils.isBlank(faturamentoOnline.getCliente().getCnpj())) {
            log.error("{} Não encontrado CNPJ (cnpjFaturarPara): {} para o processo!", Util.LOG_PREFIX,
                    faturamentoOnline.getCliente().getCnpj());
            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CNPJ_FATURAR_PARA);
        }
    }

    private String buscarNavioCues(List<InformacoesBLVO> containers) {
        return containers.stream()
                .flatMap(container -> container.getChargeableUnitEvent().stream())
                .map(EventoVO::getBexuIbId)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private List<Long> buscarGkeysServicosCues(List<InformacoesBLVO> containers) {
        return containers.stream()
                .flatMap(container -> container.getChargeableUnitEvent().stream())
                .map(EventoVO::getBexuGkey)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private String getCorpo(FaturamentoInputDTO input) {
        return "<p>Faturamento possui crossdocking e não possui evento CROSSDOCKING - " + input.getBlOriginal() + "</p>" +
                "<p>Dados do Processo: " + input + "</p>";
    }

    private void faturamentoImpoComExcecao(String cnpjFaturarPara, String blOriginal) {
        if (fatExcecaoClient.getExisteExcecao(cnpjFaturarPara, EnCategoriaFaturamento.IMPORTACAO)) {
            log.error("{} Cliente CNPJ: {} com exceção para criar Faturamento Online!", Util.LOG_PREFIX, cnpjFaturarPara);
            throw new WSFaturamentoFastException(EnTagException.EXCEPTION_CLIENT_CNPJ);
        }

        List<String> excecoesBl = fatExcecaoClient.getExcecoesBl(cnpjFaturarPara);
        if (CollectionUtils.containsAny(excecoesBl, blOriginal)) {
            log.error("{} Cliente CNPJ: {} com exceção para criar Faturamento Online pelo BL {}!", Util.LOG_PREFIX,
                    cnpjFaturarPara, blOriginal);
            throw new WSFaturamentoFastException(EnTagException.EXCEPTION_BOOKING_BL);
        }
    }
}

