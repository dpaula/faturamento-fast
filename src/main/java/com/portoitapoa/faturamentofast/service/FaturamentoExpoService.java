package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.client.impl.FatExcecaoClient;
import com.portoitapoa.faturamentofast.client.impl.FaturamentoN4Client;
import com.portoitapoa.faturamentofast.entity.Exportacao;
import com.portoitapoa.faturamentofast.entity.ExportacaoCabot;
import com.portoitapoa.faturamentofast.enuns.EnTagException;
import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import com.portoitapoa.faturamentofast.kafka.payload.PreRegisterExpoPayload;
import com.portoitapoa.faturamentofast.kafka.producer.PreRegisterExportProducer;
import com.portoitapoa.faturamentofast.model.Container;
import com.portoitapoa.faturamentofast.model.Cue;
import com.portoitapoa.faturamentofast.model.FaturamentoExportacao;
import com.portoitapoa.faturamentofast.repository.ExportacaoCabotRepository;
import com.portoitapoa.faturamentofast.repository.ExportacaoRepository;
import com.portoitapoa.faturamentofast.vo.ExportacaoVO;
import com.portoitapoa.faturamentofast.vo.FaturamentoExportacaoVO;
import com.portoitapoa.faturamentofast.vo.ProcessoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.portoitapoa.faturamentofast.util.Util;
import static com.portoitapoa.faturamentofast.util.Util.LOG_PREFIX;
import static java.lang.String.join;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaturamentoExpoService {

    private final ExportacaoRepository repository;
    private final ExportacaoCabotRepository cabotRepository;
    private final FaturamentoN4Client n4Client;
    private final FatExcecaoClient fatExcecaoClient;
    private final PreRegisterExportProducer preRegisterExportProducer;

    public Page<Exportacao> findAll(final String cnpjExportador, final String navio, final String booking,
        final Pageable pageable) {

        if (StringUtils.isNotBlank(cnpjExportador)) {
            return buscarFaturamentoExpoPorCnpjExportador(cnpjExportador, navio, booking, pageable);
        }

        if (StringUtils.isNotBlank(navio)) {
            return buscarFaturamentoExpoPorNavio(navio, booking, pageable);
        }
        if (StringUtils.isNotBlank(booking)) {
            log.info("{} Listando todos os faturamentos expo por booking: {}", LOG_PREFIX, booking);
            return repository.findAllByBooking(booking, pageable);
        }

        log.info("{} Listando todos os faturamentos expo", LOG_PREFIX);
        return repository.findAll(pageable);
    }

    private Page<Exportacao> buscarFaturamentoExpoPorNavio(final String navio, final String booking,
        final Pageable pageable) {
        log.info("{} Listando todos os faturamentos expo por navio: {}, booking: {}", LOG_PREFIX, navio, booking);
        if (StringUtils.isNotBlank(booking)) {
            return repository.findAllByNavioAndBooking(navio, booking, pageable);
        }
        return repository.findAllByNavio(navio, pageable);
    }

    private Page<Exportacao> buscarFaturamentoExpoPorCnpjExportador(final String cnpjExportador, final String navio,
        final String booking,
        final Pageable pageable) {
        if (StringUtils.isNotBlank(navio)) {
            if (StringUtils.isNotBlank(booking)) {
                log.info("{} Listando todos os faturamentos expo por cnpjExportador: {}, navio: {}, booking: {}",
                        LOG_PREFIX, cnpjExportador, navio, booking);
                return repository.findAllByCnpjExportadorAndNavioAndBooking(cnpjExportador, navio, booking,
                    pageable);
            }
            log.info("{} Listando todos os faturamentos expo por cnpjExportador: {}, navio: {}", LOG_PREFIX,
                cnpjExportador, navio);
            return repository.findAllByCnpjExportadorAndNavio(cnpjExportador, navio, pageable);
        }

        if (StringUtils.isNotBlank(booking)) {
            log.info("{} Listando todos os faturamentos expo por cnpjExportador: {}, booking: {}",
                    LOG_PREFIX, cnpjExportador, booking);
            return repository.findAllByCnpjExportadorAndBooking(cnpjExportador, booking, pageable);
        }

        log.info("{} Listando todos os faturamentos expo por cnpjExportador: {}", LOG_PREFIX, cnpjExportador);
        return repository.findAllByCnpjExportador(cnpjExportador, pageable);
    }

    public FaturamentoExportacao buscarProcessoExpo(final String cnpjExportador, final String navio,
        final String booking) {
        log.info("{} Buscando expo para faturamento, cnpjExportador: {}, navio: {}, booking: {}", LOG_PREFIX,
            cnpjExportador, navio, booking);

        List<ExportacaoVO> lstExportacao = new ArrayList<>();

        var optionalExportacao = repository.findFirstByCnpjExportadorAndNavioAndBooking(
            cnpjExportador, navio, booking);

        if (optionalExportacao.isEmpty()) {
            optionalExportacao = Optional.ofNullable(Exportacao.parse(cabotRepository.findFirstByCnpjExportadorAndNavioAndBooking(cnpjExportador, navio, booking).orElse(null)));
            if(optionalExportacao.isEmpty()) {
                log.error("{} Não encontrado processo para cnpjExportador: {}, Navio: {}, Booking: {}", LOG_PREFIX,
                    cnpjExportador, navio, booking);
                throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_PROCESS_EXPO);
            }
        }

        final var exportacao = optionalExportacao.get();

        if (exportacao.isFaturarPorBooking() ) {
            return getFaturamentoExpoPorBooking(exportacao);
        }

        final var processosExpo = repository.findAllByCnpjExportadorContainingAndNavioAndDataFaturamento(
            cnpjExportador, navio, exportacao.getDataFaturamento());

        if(!processosExpo.isEmpty()){
            return getFaturamentoExpoAgrupado(processosExpo.stream()
                    .map(ExportacaoVO::parse)
                    .collect(Collectors.toList()));
        }else{
            final var processosExpoCabot = cabotRepository.findAllByCnpjExportadorContainingAndNavioAndDataFaturamento(
                cnpjExportador, navio, exportacao.getDataFaturamento());

            if(Objects.nonNull(processosExpoCabot) && !processosExpoCabot.isEmpty()){
                for(ExportacaoCabot ec : processosExpoCabot){
                    lstExportacao.add(ExportacaoVO.parseFromCabotagem(ec));
                }
            }
        }

        return getFaturamentoExpoAgrupado(lstExportacao);
    }

    private FaturamentoExportacao getFaturamentoExpoAgrupado(final List<ExportacaoVO> processosExpo) {
        log.info("{} Buscando faturamentos de exportacao agrupados", LOG_PREFIX);
        final List<ProcessoVO> processosDue = FaturamentoExportacao.processosDueFromExportacoes(processosExpo);

        final List<String> bookings = FaturamentoExportacao.bookingsFromExportacoes(processosExpo);

        return FaturamentoExportacao.parseFromExportacaoBookigs(processosExpo.get(0), processosDue, bookings);
    }

    private FaturamentoExportacao getFaturamentoExpoPorBooking(final Exportacao exportacao) {
         return exportacao.isFaturarPorBooking() ? FaturamentoExportacao.parseFromExportacao(exportacao) : FaturamentoExportacao.parseFromExportacaoCabotagem(exportacao);
    }

    public List<Long> postFaturamentoOnline(final FaturamentoExportacao faturamentoExpo,
                                            final boolean criarExcecaoPortal,
                                            final boolean isRealUser) {

        log.info("{} Realizando post Faturamento Online, navio: {}, cnpjExportador: {}, navio: {}, booking: {}",
                LOG_PREFIX, faturamentoExpo.getNavio(), faturamentoExpo.getCliente().getCnpj(),
            faturamentoExpo.getNavio(), faturamentoExpo.getBookings().stream().collect(Collectors.joining(",")));

        // valida exceções cadastradas CNPJ ou Bookings
        faturamentoExpoComExcecao(faturamentoExpo);

        final List<Long> gkeysServicos = new ArrayList<>();

        for (final String booking : faturamentoExpo.getBookings()) {
            final var containers = n4Client.cuesProcessosN4Expo(booking, faturamentoExpo.getNavio(),
                criarExcecaoPortal);
            if (!CollectionUtils.isEmpty(containers)) {
                gkeysServicos.addAll(buscarGkeysServicosCues(containers));
            }
        }

        validaEventosCue(faturamentoExpo, gkeysServicos);

        preRegisterExportProducer.sendMessage(new PreRegisterExpoPayload(isRealUser, FaturamentoExportacaoVO.parse(faturamentoExpo)));

        return gkeysServicos;
    }

    private static void validaEventosCue(FaturamentoExportacao faturamentoExpo, List<Long> gkeysServicos) {
        if (gkeysServicos.isEmpty()) {
            log.error("{} Não retornado Eventos CUES para os bookings: {}", LOG_PREFIX, join(", ", faturamentoExpo.getBookings()));

            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CUES_BOOKING);
        }
    }

    public List<Long> postFaturamentoOnlineYard(final FaturamentoExportacao faturamentoExpo, final boolean isRealUser) {
        String cid = Util.getCorrelationId();
        log.info("{} [{}] Realizando post Faturamento Online Yard, navio: {}, cnpjExportador: {}, navio: {}, booking: {}",
                LOG_PREFIX, cid, faturamentoExpo.getNavio(), faturamentoExpo.getCliente().getCnpj(),
                faturamentoExpo.getNavio(), String.join(",", faturamentoExpo.getBookings()));

        faturamentoExpoComExcecao(faturamentoExpo);

        final List<Long> gkeyServicos = new ArrayList<>(buscarGkeysServicosCues(faturamentoExpo.getContainers()));

        validaEventosCue(faturamentoExpo, gkeyServicos);

        PreRegisterExpoPayload payload = new PreRegisterExpoPayload(isRealUser, FaturamentoExportacaoVO.parseYard(faturamentoExpo));

        preRegisterExportProducer.sendMessage(payload);

        log.info("{} [{}] Faturamento Online Yard enviado com sucesso para o tópico Kafka", LOG_PREFIX, cid);

        return gkeyServicos;
    }

    private List<Long> buscarGkeysServicosCues(final List<Container> containers) {

        return containers.stream()
            .flatMap(container -> container.getEventos().stream())
            .map(Cue::getGkeyEvento)
            .distinct()
            .sorted()
                .toList();
    }

    public void faturamentoExpoComExcecao(final FaturamentoExportacao faturamentoExpo) {
        if (StringUtils.isBlank(faturamentoExpo.getCliente().getCnpj())) {
            log.error("{} Não encontrado CNPJ do exportador: {}  para o processo!", LOG_PREFIX,
                    faturamentoExpo.getCliente().getCnpj());
            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CNPJ_EXPORTADOR);
        }

        if (fatExcecaoClient.getExisteExcecao(faturamentoExpo.getCliente().getCnpj(),
                faturamentoExpo.getCategoriaFaturamento())) {
            log.error("{} Cliente CNPJ: {} com exceção para criar Faturamento Online!", LOG_PREFIX,
                    faturamentoExpo.getCliente().getCnpj());
            throw new WSFaturamentoFastException(EnTagException.EXCEPTION_CLIENT_CNPJ);
        }

        final var excecoesBooking = fatExcecaoClient.getExcecoesBooking(faturamentoExpo.getCliente().getCnpj());
        if(CollectionUtils.containsAny(excecoesBooking, faturamentoExpo.getBookings())){
            log.error("{} Cliente CNPJ: {} com exceção para criar Faturamento Online, Bookings {}!", LOG_PREFIX,
                    faturamentoExpo.getCliente().getCnpj(), faturamentoExpo.getBookingsJoin());
            throw new WSFaturamentoFastException(EnTagException.EXCEPTION_BOOKING_BL);
        }
    }
}
