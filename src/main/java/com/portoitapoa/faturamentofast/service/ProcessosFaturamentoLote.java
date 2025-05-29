package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.client.impl.FatExcecaoClient;
import com.portoitapoa.faturamentofast.entity.ExpoBackup;
import com.portoitapoa.faturamentofast.entity.Siscomex;
import com.portoitapoa.faturamentofast.enuns.EnCategoriaFaturamento;
import com.portoitapoa.faturamentofast.enuns.EnStatusEnvioFatOnline;
import com.portoitapoa.faturamentofast.model.Cue;
import com.portoitapoa.faturamentofast.model.FaturamentoExportacao;
import com.portoitapoa.faturamentofast.repository.ExpoBackupRepository;
import com.portoitapoa.faturamentofast.util.Constants;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.EmailInputSimples;
import com.portoitapoa.faturamentofast.vo.EventoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Fernando de Lima on 16/01/21
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessosFaturamentoLote {

    private final EmailService emailService;
    private final FaturamentoSiscomexService faturamentoSiscomexService;
    private final FatExcecaoClient fatExcecaoClient;
    private final N4Service n4Service;
    private final ExpoBackupRepository expoBackupRepository;
    private final FaturamentoExpoService faturamentoExpoService;


    @Async
    public void processar(final List<Siscomex> processos) {
        processos.forEach(this::enviarProcessoFaturamento);
    }

    private void enviarProcessoFaturamento(final Siscomex processo) {
        log.info("{} Enviando procesos de faturamento para processo com bl {}, ID: {}", Util.LOG_PREFIX,
            processo.getBlOriginal(), processo.getOid());

        if (StringUtils.isBlank(processo.getCnpjFaturarPara())) {
            log.error("{} Não encontrado CNPJ do cliente para processeguir com o Faturamento, bl: {}", Util.LOG_PREFIX,
                processo.getBlOriginal());
            return;
        }

        if (fatExcecaoClient.getExisteExcecao(processo.getCnpjFaturarPara(), EnCategoriaFaturamento.IMPORTACAO)) {
            log.warn("{} Cliente com exceção para criar Faturamento Online!, bl: {}", Util.LOG_PREFIX,
                processo.getBlOriginal());
            return;
        }

        if(processo.getCrossdocking() != null && processo.getCrossdocking())  {
            final var containers = n4Service.buscarInformacoesBLPorDI(processo.getDi(), processo.getBlOriginal());
            final var cues  = containers.stream().flatMap(container -> container.getChargeableUnitEvent().stream()).map(EventoVO::getBexuEventType).collect(Collectors.toList());
            log.info("{} Eventos retornados: {}", Util.LOG_PREFIX, String.join(", ", cues));
            if(!Util.containAnyInList(cues, Constants.EVENTS_CROSSDOCKING)) {
                log.warn("{} Faturamento possui crossdocking e não possui evento  CROSSDOCKING MECANIZADO OU MANUAL!, bl: {}", Util.LOG_PREFIX, processo.getBlOriginal());
                return;
            }
        }

        if (postFaturamentoComSucesso(processo)) {
            processo.setStatusEnvioFatOnline(EnStatusEnvioFatOnline.SUCESSO);
            processo.setDataEnvioFatOnline(LocalDateTime.now(Util.ZONA_ID));
        } else {
            processo.setStatusEnvioFatOnline(EnStatusEnvioFatOnline.ERRO);
            if(processo.getDataEnvioFatOnline() == null) {
                processo.setDataEnvioFatOnline(LocalDateTime.now(Util.ZONA_ID));
            }
        }

        faturamentoSiscomexService.salvar(processo);
    }


    private boolean postFaturamentoComSucesso(final Siscomex processo) {
        try {
            faturamentoSiscomexService.postFaturamentoImportacao(processo.getBlOriginal(), false);
            return true;
        } catch (final Exception e) {
            enviarEmailErroFaturamento(processo, e);
            return false;
        }
    }

    public void enviarEmailErroFaturamento(final Siscomex processo, final Exception e) {
        log.error("{} Erro ao tentar enviar para Faturamento Online {}, erro: {}", Util.LOG_PREFIX, processo, e.getMessage());

        final var email = EmailInputSimples.builder()
                .titulo(Util.LOG_PREFIX + " - Erro Enviar Processo Faturamento Siscomex")
                .corpo(getCorpo(e, processo))
                .build();

        emailService.enviar(email);
    }

    private String getCorpo(final Exception e, final Siscomex processo) {

        return "<p>" +
            "Erro Enviar Processo Faturamento Siscomex - " +
            processo.getBlOriginal() +
            "</p>" +
            "<p>" +
            "Dados do Processo: " +
            processo +
            "</p>" +
            "<p>" +
            e.getMessage() +
            "</p>" +
            "<p>" +
            Arrays.toString(e.getStackTrace()) +
            "</p>";
    }

    private String getCorpoExpo(final Exception e, final FaturamentoExportacao processo) {

        return "<p>" +
            "Erro Enviar Processo Faturamento EXPORTACAO - " +
            processo.getBookings() +
            "</p>" +
            "<p>" +
            "Dados do Processo: " +
            processo +
            "</p>" +
            "<p>" +
            e.getMessage() +
            "</p>" +
            "<p>" +
            Arrays.toString(e.getStackTrace()) +
            "</p>";
    }

    @Async
    public void processarExpo(final List<FaturamentoExportacao> processos) {
        processos.forEach(this::enviarProcessoFaturamentoExpo);
    }

    private void enviarProcessoFaturamentoExpo(final FaturamentoExportacao exportacao) {
        log.info("{} Enviando processo de exportacao para cnpjExportador: {}, navio: {}, bookings: {}", Util.LOG_PREFIX,
            exportacao.getCliente().getCnpj(), exportacao.getNavio(), exportacao.getBookings());

        if (fatExcecaoClient.getExisteExcecao(exportacao.getCliente().getCnpj(), EnCategoriaFaturamento.EXPORTACAO)) {
            log.warn("{} Cliente com exceção para criar Faturamento Online!, cnpj: {}", Util.LOG_PREFIX,
                exportacao.getCliente().getCnpj());
            return;
        }

        final var expoBackup = criarExpoBackup(exportacao, EnStatusEnvioFatOnline.SUCESSO);
        expoBackupRepository.save(expoBackup);
    }

    public void enviarProcessoFaturamentoExpoYard(final FaturamentoExportacao exportacao) {
        String cid = Util.getCorrelationId();
        log.info("{} [{}] Enviando processo de exportacao YARD para cnpjExportador: {}, navio: {}, bookings: {}", Util.LOG_PREFIX,
                cid, exportacao.getCliente().getCnpj(), exportacao.getNavio(), exportacao.getBookings());

        log.info("{} [{}] Eventos CUEs enviados para o Yard: Containers = {}",
                Util.LOG_PREFIX,
                cid,
                exportacao.getContainers().stream()
                        .map(container -> String.format("%s%s",
                                container.getDescricao(),
                                container.getEventos().stream()
                                        .map(Cue::getGkeyEvento)
                                        .filter(Objects::nonNull)
                                        .toList()))
                        .collect(Collectors.joining(", "))
        );

        List<Long> faturamentoExpoComSucesso = postFaturamentoExpoComSucesso(exportacao);

        if (CollectionUtils.isEmpty(faturamentoExpoComSucesso)) {
            final var expoBackup = criarExpoBackup(exportacao, EnStatusEnvioFatOnline.ERRO);
            expoBackupRepository.save(expoBackup);
            log.warn("{} [{}] Falha ao processar faturamento de exportação YARD", Util.LOG_PREFIX, cid);
            return;
        }

        final var expoBackup = criarExpoBackup(exportacao, EnStatusEnvioFatOnline.SUCESSO);
        expoBackupRepository.save(expoBackup);
        log.info("{} [{}] Faturamento de exportação YARD processado com sucesso", Util.LOG_PREFIX, cid);
    }

    private List<Long> postFaturamentoExpoComSucesso(final FaturamentoExportacao exportacao) {
        String cid = Util.getCorrelationId();
        try {
            log.info("{} [{}] Iniciando envio para Faturamento exportacao {}", Util.LOG_PREFIX, cid, exportacao.getBookingsJoin());
            if (exportacao.isYard()) {
                return faturamentoExpoService.postFaturamentoOnlineYard(exportacao, false);
            }
            return faturamentoExpoService.postFaturamentoOnline(exportacao, true, false);

        } catch (final Exception e) {
            log.error("{} [{}] Erro ao tentar enviar para Faturamento exportacao {}, erro: {}", 
                Util.LOG_PREFIX, cid, exportacao.getBookingsJoin(), e.getMessage());

            final var email = EmailInputSimples.builder()
                .titulo(Util.LOG_PREFIX + " [" + cid + "] - Erro Enviar Processo Faturamento Exportação")
                .corpo(getCorpoExpo(e, exportacao))
                .build();

            emailService.enviar(email);

            return List.of();
        }
    }

    private ExpoBackup criarExpoBackup(final FaturamentoExportacao exportacao, final EnStatusEnvioFatOnline sucesso) {
        return ExpoBackup.builder()
            .dataFaturamento(exportacao.getDataFaturamento())
            .bookings(exportacao.getBookingsJoin())
            .cnpjExportador(exportacao.getCliente().getCnpj())
            .dataEnvioFatOnline(LocalDateTime.now(Util.ZONA_ID))
            .faturarPorBooking(exportacao.isFaturarPorBooking())
            .navio(exportacao.getNavio())
            .statusEnvioFatOnline(sucesso)
            .gkeysCues(exportacao.getGkeyServicosJoin())
            .build();
    }
}
