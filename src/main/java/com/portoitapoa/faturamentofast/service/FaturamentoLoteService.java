package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.client.impl.FatExcecaoClient;
import com.portoitapoa.faturamentofast.entity.Exportacao;
import com.portoitapoa.faturamentofast.entity.ExportacaoCabot;
import com.portoitapoa.faturamentofast.entity.ExportacaoYard;
import com.portoitapoa.faturamentofast.entity.Siscomex;
import com.portoitapoa.faturamentofast.model.Container;
import com.portoitapoa.faturamentofast.model.Cue;
import com.portoitapoa.faturamentofast.model.FaturamentoExportacao;
import com.portoitapoa.faturamentofast.repository.*;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.ExportacaoVO;
import com.portoitapoa.faturamentofast.vo.ProcessoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaturamentoLoteService {

    private static final int QTD_HORAS_LIBERACAO = 60;
    private static final String STATUS_LIBERADO = "Liberado";

    private final SiscomexRepository siscomexRepository;
    private final ExportacaoRepository exportacaoRepository;
    private final ExpoBackupRepository expoBackupRepository;
    private final ExportacaoYardRepository exportacaoYardRepository;
    private final ProcessosFaturamentoLote processosLote;
    private final FatExcecaoClient fatExcecaoClient;

    private final ExportacaoCabotRepository exportacaoCabotRepository;

    public List<String> postFaturamentoSiscomexImpoLote(Integer qtdHoras, Integer delayMinutos) {
        qtdHoras = usarQtdHorasRetroativas(qtdHoras) ? qtdHoras : QTD_HORAS_LIBERACAO;
        log.info("{} Processando faturamentos siscomex para processos de {} horas atrás, que ainda não foi enviado para faturamento online",
                Util.LOG_PREFIX, qtdHoras);
        final var dataLiberacaoMinima = LocalDateTime.now(Util.ZONA_ID).minusHours(qtdHoras);
        final var dataLiberacaoMaxima = LocalDateTime.now(Util.ZONA_ID).minusMinutes(Objects.isNull(delayMinutos) ? 0 : delayMinutos);
        final LocalDateTime dataRetentar = LocalDateTime.now(Util.ZONA_ID).minusMinutes(30);
        final List<Siscomex> processos = siscomexRepository.findAllForProcess(STATUS_LIBERADO, dataLiberacaoMinima, dataLiberacaoMaxima, dataRetentar);
        processosLote.processar(processos);
        return processos.stream()
            .map(Siscomex::getBlOriginal)
            .collect(Collectors.toList());
    }

    public List<String> postFaturamentoSiscomexImpoCrossDockingLote(Integer qtdDias) {

        qtdDias = usarQtdDiasRetroativas(qtdDias);
        log.info("{} Processando faturamentos siscomex com crossdocking para os últimos {} dias", Util.LOG_PREFIX, qtdDias);

        final var dataLiberacao = LocalDateTime.now(Util.ZONA_ID).minusDays(qtdDias);

//        final var processos = siscomexRepository.findAllByParteAndStatusAndDataLiberacaoIsGreaterThanEqualAndDataEnvioFatOnlineIsNullAndCrossdockingIsTrue(
//                false,
//                STATUS_PROCESSO, dataLiberacao);


        final LocalDateTime dataRetentar = LocalDateTime.now(Util.ZONA_ID).minusMinutes(30);
        final List<Siscomex> processos = siscomexRepository.findAllForProcessCrossDocking(false, STATUS_LIBERADO, dataLiberacao, dataRetentar);

        processosLote.processar(processos);

        return processos.stream()
                .map(Siscomex::getBlOriginal)
                .collect(Collectors.toList());

    }

    private Integer usarQtdDiasRetroativas(final Integer qtdDias) {

        if (qtdDias == null || qtdDias < 0) {
            return 0;
        }

        return  qtdDias > 30 ? 30 : qtdDias;
    }

    private boolean usarQtdHorasRetroativas(final Integer qtdHoras) {

        if (qtdHoras == null) {
            return false;
        }

        return qtdHoras >= 0 && qtdHoras <= 48;
    }

    public List<String> postFaturamentoExpoLote(final Integer qtdHoras, final Integer qtdFaturar) {
        log.info("{} Iniciando processo de post dos faturamentos de exportacao em lote para as últimas {} horas e quantidade para faturar: {}", Util.LOG_PREFIX, qtdHoras, qtdFaturar);

        List<Exportacao> processos = getProcessosExportacoes(qtdHoras);
        List<ExportacaoCabot> processosCabot = getProcessosExportacoesCabot(qtdHoras);

        if (CollectionUtils.isEmpty(processos) && CollectionUtils.isEmpty(processosCabot)) {
            log.warn("{} Não encontrado registros de exportacao para processo em lote", Util.LOG_PREFIX);
            return List.of();
        }

        log.info("{} Foram encontrados {} registros de exportacao para processo em lote", Util.LOG_PREFIX,
            processos.size());

        if(qtdFaturar != null && processos.size() > qtdFaturar) {
            log.info("{} Faturando apenas {} registros de exportacao do total de {}", Util.LOG_PREFIX, qtdFaturar, processos.size());
            processos = processos.subList(0, qtdFaturar);
        }

        if(qtdFaturar != null && processosCabot.size() > qtdFaturar) {
            log.info("{} Faturando apenas {} registros de exportacao cabotagem do total de {}", Util.LOG_PREFIX, qtdFaturar, processosCabot.size());
            processosCabot = processosCabot.subList(0, qtdFaturar);
        }

        for(ExportacaoCabot ec : processosCabot){
            processos.add(Exportacao.parse(ec));
        }

        var faturamentoExportacoes = filtrarFaturamentoExportacao(processos.stream()
                .map(ExportacaoVO::parse)
                .collect(Collectors.toList())
        );
        log.info("{} Serão faturados: {} processos após as remocoes", Util.LOG_PREFIX, faturamentoExportacoes.size());
        processosLote.processarExpo(faturamentoExportacoes);

        return faturamentoExportacoes.stream()
            .flatMap(f -> f.getBookings().stream())
            .collect(Collectors.toList());
    }

    public List<String> postFaturamentoExpoLotePorNavio(final Integer qtdHoras, final String navio) {
        log.info("{} Iniciando processo de post dos faturamentos de exportacao por navio em lote para as últimas {} horas e navio: {}", Util.LOG_PREFIX, qtdHoras, navio);

        List<Exportacao> processos = getProcessosExportacoesPorNavio(qtdHoras, navio);
        if (CollectionUtils.isEmpty(processos)) {
            log.warn("{} Não encontrado registros de exportacao para processo em lote para o navio: {}", Util.LOG_PREFIX, navio);
            return List.of();
        }

        log.info("{} Foram encontrados {} registros de exportacao para processo em lote para o navio: {}", Util.LOG_PREFIX,
                processos.size(), navio);

        var faturamentoExportacoes = filtrarFaturamentoExportacao(processos.stream()
                .map(ExportacaoVO::parse)
                .collect(Collectors.toList())
        );
        log.info("{} Serão faturados: {} processos após as remocoes para o navio: {}", Util.LOG_PREFIX, faturamentoExportacoes.size(), navio);
        processosLote.processarExpo(faturamentoExportacoes);

        return faturamentoExportacoes.stream()
                .flatMap(f -> f.getBookings().stream())
                .collect(Collectors.toList());
    }

    public FaturamentoExportacao postFaturamentoExpoLoteYard(final Integer qtdHoras) {
        log.info("{} Processando Exportacoes Yard para as ultimas {} horas", Util.LOG_PREFIX, qtdHoras);

        final List<ExportacaoYard> processos = getProcessosExportacaoYard(qtdHoras);
        if (CollectionUtils.isEmpty(processos)) {
            log.warn("{} Não encontrado registros de Exportacao Yard.", Util.LOG_PREFIX);
            return null;
        }

        processos.removeIf(p -> expoBackupRepository.existsByCnpjExportadorAndNavioAndBookingsContaining(
                p.getCnpjExportador(),
                p.getNavio(), p.getBooking()));

        if (CollectionUtils.isEmpty(processos)) {
            log.warn("{} Não encontrado novos registros de Exportacao Yard", Util.LOG_PREFIX);
            return null;
        }

        String cid = Util.generateCorrelationId();
        log.info("{} [{}] Foram encontrados {} registros de Exportacao Yard para o booking: {}",
                Util.LOG_PREFIX, cid, processos.size(), processos.get(0).getBooking());
        final List<ExportacaoVO> exportacaoVOList = processos.stream()
                .map(ExportacaoVO::parseFromYard)
                .toList();

        FaturamentoExportacao faturamentoExportacao = getFaturamentoExportacao(exportacaoVOList);

        log.info("{} [{}] Objeto FaturamentoExportacao criado com sucesso para o navio: {} e {} containers envolvidos.",
                Util.LOG_PREFIX, cid, faturamentoExportacao.getNavio(), faturamentoExportacao.getContainers().size());

        processosLote.enviarProcessoFaturamentoExpoYard(faturamentoExportacao);

        return faturamentoExportacao;
    }

    private static FaturamentoExportacao getFaturamentoExportacao(List<ExportacaoVO> exportacaoVOList) {
        log.info("{} Criando objeto FaturamentoExportacao para Yard", Util.LOG_PREFIX);

        var processosAgrupadosPorContainer = exportacaoVOList.stream()
                .collect(Collectors.groupingBy(ExportacaoVO::getContainer));

        String booking = exportacaoVOList.get(0).getBooking();

        log.info("{} Agrupando processos por container para booking: [{}], total containers: {}", Util.LOG_PREFIX, booking, processosAgrupadosPorContainer.size());
        log.debug("{} Processos por containers agrupados detalhados para booking: [{}], -> {}", Util.LOG_PREFIX, booking, processosAgrupadosPorContainer);

        var processosCueYardPorContainer = processosAgrupadosPorContainer.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(Cue::parse)
                                .toList()
                ));

        log.info("{} Agrupando processos Cues por containers para booking: [{}], total containers: {}", Util.LOG_PREFIX, booking, processosCueYardPorContainer.size());
        log.debug("{} Processos Cues por containers agrupados detalhados para booking: [{}], -> {}", Util.LOG_PREFIX, booking, processosCueYardPorContainer);

        List<Container> containersYard = processosCueYardPorContainer.entrySet().stream()
                .map(entry -> Container.builder()
                        .descricao(entry.getKey())  // Container como descrição
                        .eventos(new HashSet<>(entry.getValue())) // eventos do CueYard
                        .build()
                )
                .toList();

        log.info("{} Criando lista de containers para o booking: [{}], total containers: {}", Util.LOG_PREFIX, booking, containersYard.size());
        log.debug("{} Lista de containers detalhados para o booking: [{}], -> {}", Util.LOG_PREFIX, booking, containersYard);

        ExportacaoVO referenciaExportacao = exportacaoVOList.get(0);
        FaturamentoExportacao faturamentoExportacao =
                FaturamentoExportacao.parseFromExportacaoYard(referenciaExportacao, containersYard);

        log.info("{} Criando objeto FaturamentoExportacao para o booking: [{}], total containers: {}", Util.LOG_PREFIX, booking, containersYard.size());
        log.info("{} Objeto FaturamentoExportacao detalhado para o booking: [{}], -> {}", Util.LOG_PREFIX, booking, faturamentoExportacao);

        return faturamentoExportacao;
    }


    private List<FaturamentoExportacao> getProcessosPorBooking(final List<ExportacaoVO> processos) {
        final var processosAgrupadosPorBooking = processos.stream()
                .filter(ExportacaoVO::isFaturarPorBooking)
                .collect(Collectors.groupingBy(ExportacaoVO::getBooking));

        List<FaturamentoExportacao> faturamentos = processosAgrupadosPorBooking.values().stream()
                .map(lista -> lista.get(0))
                .map(FaturamentoExportacao::parseFromExportacao)
                .collect(Collectors.toList());

        log.info("{} Do total de {} registros de expo, {} são para faturar por booking", Util.LOG_PREFIX,
                processos.size(), faturamentos.size());

        return faturamentos;
    }

    private List<Exportacao> getProcessosExportacoes(final Integer qtdHoras) {

        List<Exportacao> lstExportacao = new ArrayList<>();

        if (qtdHoras != null) {
            log.info(
                "{} Processando faturamentos exportacao para processos de {} horas atrás, que ainda não foi enviado para faturamento online",
                Util.LOG_PREFIX, qtdHoras);
            final var dataFaturamento = LocalDateTime.now(Util.ZONA_ID).minusHours(qtdHoras);

            lstExportacao.addAll(exportacaoRepository.findAllByDataFaturamentoIsGreaterThanEqualAndCnpjExportadorNotNullAndNavioNotNull(
                dataFaturamento));

            return lstExportacao;
        }
        log.info(
            "{} Processando faturamentos exportacao para processos de {} horas atrás, que ainda não foi enviado para faturamento online",
            Util.LOG_PREFIX, QTD_HORAS_LIBERACAO);
        final var dataFaturamento = LocalDateTime.now(Util.ZONA_ID).minusHours(QTD_HORAS_LIBERACAO);

        return exportacaoRepository.findAllByDataFaturamentoIsGreaterThanEqualAndCnpjExportadorNotNullAndNavioNotNull(
            dataFaturamento);
    }

    private List<Exportacao> getProcessosExportacoesPorNavio(final Integer qtdHoras, final String navio) {
            log.info(
                    "{} Processando faturamentos exportacao para processos de {} horas atrás e navio: {}, que ainda não foi enviado para faturamento online",
                    Util.LOG_PREFIX, qtdHoras, navio);
            final var dataFaturamento = LocalDateTime.now(Util.ZONA_ID).minusHours(qtdHoras);
            return exportacaoRepository.findAllByDataFaturamentoIsGreaterThanEqualAndCnpjExportadorNotNullAndNavio(
                    dataFaturamento, navio);
    }

    private List<ExportacaoYard> getProcessosExportacaoYard(final Integer qtdHoras) {
        log.info("{} Processando Faturamentos Exportacao Yard de {} horas atrás que ainda não foram enviados para Faturamento Online",
                Util.LOG_PREFIX, qtdHoras);

        final var dataLiberacaoFaturamento = LocalDateTime.now(Util.ZONA_ID).minusHours(qtdHoras);
        return exportacaoYardRepository.filtroPorDataExportacao(dataLiberacaoFaturamento);
    }

    private List<ExportacaoCabot> getProcessosExportacoesCabot(final Integer qtdHoras) {

        List<ExportacaoCabot> lstExportacaoCabot = new ArrayList<>();

        if (qtdHoras != null) {
            log.info(
                "{} Processando faturamentos exportacao para processos de {} horas atrás, que ainda não foi enviado para faturamento online",
                Util.LOG_PREFIX, qtdHoras);
            final var dataFaturamento = LocalDateTime.now(Util.ZONA_ID).minusHours(qtdHoras);

            lstExportacaoCabot.addAll(exportacaoCabotRepository.findAllByDataFaturamentoIsGreaterThanEqualAndCnpjFatCabotagemNotNullAndNavioNotNullAndIsCabotIsTrue(
                dataFaturamento));

            return lstExportacaoCabot;
        }
        log.info(
            "{} Processando faturamentos exportacao para processos de {} horas atrás, que ainda não foi enviado para faturamento online",
            Util.LOG_PREFIX, QTD_HORAS_LIBERACAO);
        final var dataFaturamento = LocalDateTime.now(Util.ZONA_ID).minusHours(QTD_HORAS_LIBERACAO);

        return exportacaoCabotRepository.findAllByDataFaturamentoIsGreaterThanEqualAndCnpjExportadorNotNullAndNavioNotNull(
            dataFaturamento);
    }


    private List<FaturamentoExportacao> converterProcessosAgrupadosParaFaturamento(final List<ExportacaoVO> processoAgrupados) {
        final var collect = processoAgrupados.stream()
            .collect(Collectors.groupingBy(ExportacaoVO::getCnpjExportador, Collectors.groupingBy(ExportacaoVO::getNavio, Collectors.groupingBy(ExportacaoVO::getDataFaturamento))));

        final List<FaturamentoExportacao> faturamentos = new ArrayList<>();

        collect.forEach((cnpjExportador, v) -> v.forEach((navio, b) -> b.forEach((dataFaturamento, valor) -> {
            final List<ProcessoVO> processosDue = FaturamentoExportacao.processosDueFromExportacoes(valor);
            final List<String> bookings = FaturamentoExportacao.bookingsFromExportacoes(valor);

            faturamentos.add(FaturamentoExportacao.parseFromExportacaoBookigs(valor.get(0), processosDue, bookings));

        })));
        return faturamentos;
    }

    public List<FaturamentoExportacao> postFaturamentoExpoLotePrint(final Integer qtdHoras) {
        log.info("{} Iniciando processo de post dos faturamentos de exportacao em lote print para as últimas {} horas", Util.LOG_PREFIX, qtdHoras);
        final List<Exportacao> processos = getProcessosExportacoes(qtdHoras);

        if (CollectionUtils.isEmpty(processos)) {
            log.warn("{} Não encontrado registros de exportacao para processo em lote", Util.LOG_PREFIX);
            return List.of();
        }
        log.info("{} Foram encontrados {} registros de exportacao para processo em lote", Util.LOG_PREFIX,
            processos.size());

        return filtrarFaturamentoExportacao(processos.stream()
                .map(ExportacaoVO::parse)
                .collect(Collectors.toList())
        );
    }

    private List<FaturamentoExportacao> filtrarFaturamentoExportacao(final List<ExportacaoVO> processos) {
        final List<FaturamentoExportacao> processosExpos = getProcessosPorBooking(processos);
        final List<ExportacaoVO> naoFaturaveisPorBooking = agruparPorNaoFaturaveisPorBooking(processos);

        processosExpos.addAll(converterProcessosAgrupadosParaFaturamento(naoFaturaveisPorBooking));

        final var faturamentosNaoProcessados =  processosExpos.stream()
                .filter(processo -> !expoBackupRepository.existsByCnpjExportadorAndNavioAndBookingsContaining(
                        processo.getCliente().getCnpj(),
                        processo.getNavio(), processo.getBookings().get(0)))
                .collect(Collectors.toList());

        log.info("{} Faturamentos não processados de acordo com a tabela expo backup: {}", Util.LOG_PREFIX, faturamentosNaoProcessados.size());
        return removerProcessosEventosExcecao(faturamentosNaoProcessados);
    }

    private List<ExportacaoVO> agruparPorNaoFaturaveisPorBooking(final List<ExportacaoVO> processos) {
        return processos.stream()
            .filter(exportacao -> !exportacao.isFaturarPorBooking())
            .collect(Collectors.toList());
    }

    private List<FaturamentoExportacao> removerProcessosEventosExcecao(
        final List<FaturamentoExportacao> processosExpos) {
        log.info("{} Iniciando processo de remoção de eventos exceção", Util.LOG_PREFIX);
        final List<FaturamentoExportacao> processosSemExcecaoEventos = new ArrayList<>();

        for (final FaturamentoExportacao faturamentoExportacao : processosExpos) {
            if (!faturamentoLoteComExcecao(faturamentoExportacao)) {
                processosSemExcecaoEventos.add(faturamentoExportacao);
            } else {
                log.warn("{} Processo de faturamento de exportacao com excecao: {}", Util.LOG_PREFIX,
                    faturamentoExportacao);
            }
        }

        return processosSemExcecaoEventos;
    }

    public boolean faturamentoLoteComExcecao(final FaturamentoExportacao faturamentoExpo) {
        if (fatExcecaoClient.getExisteExcecao(faturamentoExpo.getCliente().getCnpj(),
                faturamentoExpo.getCategoriaFaturamento())) {
            return true;
        }

        final var excecoesBooking = fatExcecaoClient.getExcecoesBooking(faturamentoExpo.getCliente().getCnpj());

        return CollectionUtils.containsAny(excecoesBooking, faturamentoExpo.getBookings());
    }
}
