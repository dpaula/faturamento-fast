package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.client.impl.CabotagemImpoClient;
import com.portoitapoa.faturamentofast.client.impl.FatExcecaoClient;
import com.portoitapoa.faturamentofast.client.impl.ManagerClient;
import com.portoitapoa.faturamentofast.entity.Siscomex;
import com.portoitapoa.faturamentofast.enuns.EnCategoriaFaturamento;
import com.portoitapoa.faturamentofast.enuns.EnTagException;
import com.portoitapoa.faturamentofast.error.ResourceNotFoundException;
import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import com.portoitapoa.faturamentofast.kafka.payload.CabotagemImportPayload;
import com.portoitapoa.faturamentofast.kafka.producer.CabotagemImportProducer;
import com.portoitapoa.faturamentofast.model.Solicitacao;
import com.portoitapoa.faturamentofast.repository.ImportacaoDepartedRepository;
import com.portoitapoa.faturamentofast.repository.SiscomexRepository;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.EventoVO;
import com.portoitapoa.faturamentofast.vo.FaturamentoSiscomexVO;
import com.portoitapoa.faturamentofast.vo.InformacoesBLVO;
import com.portoitapoa.faturamentofast.vo.SolicitacaoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaturamentoSiscomexService {

    private final SiscomexRepository repository;
    private final N4Service n4Service;
    private final FatExcecaoClient fatExcecaoClient;
    private final CockPitService cockPitService;
    private final CabotagemImportProducer cabotagemImportProducer;
    private final CabotagemImpoClient cabotagemImpoClient;
    private final ImportacaoDepartedRepository importacaoDepartedRepository;
    private final ManagerClient managerClient;
    private final FaturamentoProcessImportService faturamentoProcessImportService;

    public Page<Siscomex> findAll(final Pageable pageable) {
        log.info("{} Listando todos os faturamentos siscomex", Util.LOG_PREFIX);

        return repository.findAll(pageable);
    }

    public Siscomex findByIdOrThrow(final Integer id) {
        log.info("{} Buscando faturamento siscomex de importação para o ID {}", Util.LOG_PREFIX, id);

        Optional<Siscomex> res = repository.findById(id);
        if(!res.isPresent()) {
            log.warn("{} Faturamento Siscomex não encontrado com o id: {}", Util.LOG_PREFIX, id);
            throw new ResourceNotFoundException(EnTagException.NOT_FOUND_SISCOMEX_FAT_ID);
        }
        return res.get();
    }

    public Siscomex findSiscomexByNumeroBl(final String numeroBl) {
        log.info("{} Listando faturamento siscomex de importação para BL {}", Util.LOG_PREFIX, numeroBl);
        final var siscomex = buscarSiscomex(numeroBl);
        if (nonNull(siscomex)) {
            final var containers = n4Service.buscarInformacoesBLPorDI(siscomex.getDi(), numeroBl);
//            final var containers = n4Client.cuesProcessosN4Impo(siscomex.getDi(), numeroBl);
            siscomex.setNavio(buscarNavioCues(containers));
        }
        return siscomex;
    }

    public FaturamentoSiscomexVO postFaturamentoImportacao(final String numeroBl, final boolean isRealUser) {
        log.info("{} Preparando para post siscomex/cabotagem de importação para BL {}", Util.LOG_PREFIX, numeroBl);
        Siscomex siscomex = buscarSiscomex(numeroBl);
        if (nonNull(siscomex)) {
            return faturamentoProcessImportService.siscomexImport(siscomex, isRealUser);
        }
        SolicitacaoVO solicitacaoVO = buscarCabotagem(numeroBl);
        if (nonNull(solicitacaoVO)) {
            return FaturamentoSiscomexVO.parse(postFaturamentoCabotagemImportacao(solicitacaoVO, isRealUser));
        }
        var process = managerClient.findFinishedProcessByBl(numeroBl);
        if (Objects.nonNull(process)) {
            return faturamentoProcessImportService.processManagerImport(process, isRealUser);
        }
        log.warn("{} Não encontrado faturamento siscomex/cabotagem para BL: {}", Util.LOG_PREFIX, numeroBl);
        throw new ResourceNotFoundException(EnTagException.NOT_FOUND_SISCOMEX_CABOTAGEM_FAT_BL);
    }

    private SolicitacaoVO buscarCabotagem(String numeroBl) {
        Pageable pageable = PageRequest.ofSize(1);
        return cabotagemImpoClient.filter(numeroBl, pageable);
    }

    public Solicitacao postFaturamentoCabotagemImportacao(final SolicitacaoVO solicitacaoVO, final boolean isRealUser) {
        Solicitacao solicitacao = new Solicitacao(solicitacaoVO);
        if (StringUtils.isBlank(solicitacao.getFaturarPara())) {
            log.error("{} Não encontrado [CNPJ Faturar Para] para processeguir com o Faturamento!", Util.LOG_PREFIX);
            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CNPJ_FATURAR_PARA);
        }

        // valida exceções cadastradas CNPJ ou BL
        faturamentoImpoComExcecao(solicitacao.getFaturarPara(), solicitacao.getProcesso());

        final var containers = n4Service.buscarInformacoesBLPorCE(solicitacao.getCe(), solicitacao.getProcesso());
//        final var containers = n4Client.cuesProcessosN4Impo(solicitacao.getCe(), solicitacao.getProcesso());

        if (containers.isEmpty()) {
            log.error("{} Não retornado Eventos CUES para o BL [{}]", Util.LOG_PREFIX, solicitacao.getProcesso());
            throw new WSFaturamentoFastException(EnTagException.NOT_FOUND_CUES_BL);
        }

        solicitacao.setNavio(buscarNavioCues(containers));

        final var gkeys = buscarGkeysServicosCues(containers);

        cockPitService.enviarEventoExcecao(gkeys);

        log.info("{} Novo post de faturamento cabotagem para Faturamento Online: {}", Util.LOG_PREFIX,
                solicitacao.getProcesso());

        cabotagemImportProducer.sendMessage(new CabotagemImportPayload(isRealUser, solicitacao));

        return solicitacao;
    }

    public Siscomex putAtualizarFaturamentoSiscomex(final Integer id, final boolean crossdocking) {
        log.info("{} Atualizando o crossdocking do siscomex com id {}", Util.LOG_PREFIX, id);

        final Siscomex siscomex = findByIdOrThrow(id);
        if(crossdocking == siscomex.getCrossdocking()) {
            return siscomex;
        }
        siscomex.setCrossdocking(crossdocking);
        return repository.save(siscomex);
    }

    private Siscomex buscarSiscomex(final String numeroBl) {
        Optional<Siscomex> res = repository.findFirstByBlOriginalAndDataLiberacaoIsNotNull(numeroBl);
        return res.orElse(null);
    }

    private String buscarNavioCues(final List<InformacoesBLVO> containers) {
        return containers.stream()
                .map(InformacoesBLVO::getChargeableUnitEvent)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(EventoVO::getBexuIbId)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private List<Long> buscarGkeysServicosCues(final List<InformacoesBLVO> containers) {
        return containers.stream()
                .flatMap(container -> container.getChargeableUnitEvent().stream())
                .map(EventoVO::getBexuGkey)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public void salvar(final Siscomex siscomex) {
        log.info("{} Salvando siscomex {}", Util.LOG_PREFIX, siscomex.getBlOriginal());
        repository.save(siscomex);
    }

    public Boolean buscarCrossDocking(final Integer id) {
        log.info("{} Buscando crossdocking do siscomex {}", Util.LOG_PREFIX, id);

        final Siscomex siscomex = findByIdOrThrow(id);
        return siscomex.getCrossdocking() == null ? false : siscomex.getCrossdocking();
    }

    public void faturamentoImpoComExcecao(String cnpjFaturarPara, String blOriginal) {

        if (fatExcecaoClient.getExisteExcecao(cnpjFaturarPara, EnCategoriaFaturamento.IMPORTACAO)) {
            log.error("{} Cliente CNPJ: {} com exceção para criar Faturamento Online!", Util.LOG_PREFIX,
                cnpjFaturarPara);
            throw new WSFaturamentoFastException(EnTagException.EXCEPTION_CLIENT_CNPJ);
        }

        final var excecoesBl = fatExcecaoClient.getExcecoesBl(cnpjFaturarPara);

        if (CollectionUtils.containsAny(excecoesBl, blOriginal)) {
            log.error("{} Cliente CNPJ: {} com exceção para criar Faturamento Online pelo BL {}!", Util.LOG_PREFIX,
                cnpjFaturarPara, blOriginal);
            throw new WSFaturamentoFastException(EnTagException.EXCEPTION_BOOKING_BL);
        }
    }

    public List<InformacoesBLVO> buscarInfoN4(String bl, String di, String ce) {
        if(ce != null)
            return n4Service.buscarInformacoesBLPorCE(ce, bl);
        else return n4Service.buscarInformacoesBLPorDI(di, bl);
    }

    public List<String> processarImpoLoteDeparted(Integer qtdHoras) {
        log.info("{} Processando lote DEPARTED de importação. qtdHoras: {}", Util.LOG_PREFIX, qtdHoras);

        final var data = Objects.isNull(qtdHoras) ? null : LocalDateTime.now(Util.ZONA_ID).minusHours(qtdHoras);

        Set<Tuple> numerosBl = Objects.isNull(qtdHoras) ? importacaoDepartedRepository.findAllNumerosBl() : importacaoDepartedRepository.findAllNumerosBlPorHoras(data);
        if(!numerosBl.isEmpty()) {
            log.info("{} Lote DEPARTED. Foram encontrados: {} bls para serem processados", Util.LOG_PREFIX, numerosBl.size());
        }

        processarImpoLoteDeparted(numerosBl);

        return numerosBl.stream().map(tuple -> tuple.get("numeroBl", String.class)).collect(Collectors.toList());
    }

    @Async
    public void processarImpoLoteDeparted(Set<Tuple> numerosBl) {
        for (Tuple tuple : numerosBl) {
            try {
                String numeroBl = tuple.get("numeroBl", String.class);
                String docTipoAduaneiro = tuple.get("docTipoAduaneiro", String.class);
                log.info("{} Lote DEPARTED. Processando bl: {}", Util.LOG_PREFIX, numeroBl);
                if ("CTAC".equals(docTipoAduaneiro)) {
                    SolicitacaoVO solicitacaoVO = buscarCabotagem(numeroBl);
                    if (nonNull(solicitacaoVO)) {
                        postFaturamentoCabotagemImportacao(solicitacaoVO, true);
                    }
                } else {
                    Siscomex siscomex = buscarSiscomex(numeroBl);
                    if (nonNull(siscomex)) {
                        faturamentoProcessImportService.siscomexImport(siscomex, true);
                    }
                }
            } catch(Exception e){
                log.error("{} Erro ao processar lote DEPARTED: {}", Util.LOG_PREFIX, e.getMessage());
            }
        }
    }
}
