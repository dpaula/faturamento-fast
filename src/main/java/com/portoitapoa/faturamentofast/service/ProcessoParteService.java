package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.entity.Siscomex;
import com.portoitapoa.faturamentofast.enuns.EnStatusEnvioFatOnline;
import com.portoitapoa.faturamentofast.repository.SiscomexRepository;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessoParteService {

    private static final int QTD_HORAS_LIBERACAO = 60;
    private static final String STATUS_LIBERADO = "Liberado";

    private final SiscomexRepository siscomexRepository;
    private final FaturamentoSiscomexService faturamentoSiscomexService;
    private final ProcessosFaturamentoLote processosFaturamentoLote;
    private final FaturamentoProcessImportService faturamentoProcessImportService;

    public List<String> gerarFaturamentos(final Integer quantidadeHoras, final Integer delayMinutos) {
        final var dataEnvioFaturamentoMinima = obterDataLiberacaoMaxima(quantidadeHoras);
        final var dataEnvioFaturamentoMaxima = obterDataLiberacaoMinima(delayMinutos);
        final var dataReenvioFaturamento = obterDataMaximaDeEnvioFaturamentoOnline();

        log.info("{} Processando faturamentos Siscomex para processos liberados a partir de {} até {}, que ainda não foi enviados para faturamento online ou que tiveram erro.",
                Util.LOG_PREFIX, dataEnvioFaturamentoMinima, dataEnvioFaturamentoMaxima);

        final List<Siscomex> processosParte = siscomexRepository.listarProcessosParte(
                STATUS_LIBERADO,
                dataEnvioFaturamentoMinima,
                dataEnvioFaturamentoMaxima,
                dataReenvioFaturamento
        );

        processar(processosParte);

        return processosParte.stream()
                .map(Siscomex::getBlOriginal)
                .collect(Collectors.toList());
    }

    private LocalDateTime obterDataLiberacaoMaxima(final Integer quantidadeHoras) {
        if (quantidadeHoras != null && quantidadeHoras >= 0 && quantidadeHoras <= 48) {
            return LocalDateTime.now(Util.ZONA_ID).minusHours(quantidadeHoras);
        }

        return LocalDateTime.now(Util.ZONA_ID).minusHours(QTD_HORAS_LIBERACAO);
    }

    private LocalDateTime obterDataMaximaDeEnvioFaturamentoOnline() {
        return LocalDateTime.now(Util.ZONA_ID).minusMinutes(30);
    }

    private LocalDateTime obterDataLiberacaoMinima(Integer delay) {
        return LocalDateTime.now(Util.ZONA_ID).minusMinutes(Objects.isNull(delay) ? 0 : delay);
    }

    @Async
    public void processar(final List<Siscomex> processos) {
        processos.forEach(processo -> {
            try {
                faturamentoProcessImportService.siscomexImport(processo, false);
                processo.setStatusEnvioFatOnline(EnStatusEnvioFatOnline.SUCESSO);
                processo.setDataEnvioFatOnline(LocalDateTime.now(Util.ZONA_ID));
            } catch (Exception e) {
                processo.setStatusEnvioFatOnline(EnStatusEnvioFatOnline.ERRO);
                if (processo.getDataEnvioFatOnline() == null) {
                    processo.setDataEnvioFatOnline(LocalDateTime.now(Util.ZONA_ID));
                }

                processosFaturamentoLote.enviarEmailErroFaturamento(processo, e);
            }

            faturamentoSiscomexService.salvar(processo);
        });
    }


}
