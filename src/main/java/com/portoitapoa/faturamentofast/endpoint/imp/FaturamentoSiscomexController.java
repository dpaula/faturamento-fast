package com.portoitapoa.faturamentofast.endpoint.imp;

import com.portoitapoa.faturamentofast.client.impl.CabotagemImpoClient;
import com.portoitapoa.faturamentofast.client.impl.ManagerClient;
import com.portoitapoa.faturamentofast.endpoint.IFaturamentoSiscomexController;
import com.portoitapoa.faturamentofast.enuns.EnTagException;
import com.portoitapoa.faturamentofast.error.ResourceNotFoundException;
import com.portoitapoa.faturamentofast.service.FaturamentoLoteService;
import com.portoitapoa.faturamentofast.service.FaturamentoSiscomexService;
import com.portoitapoa.faturamentofast.service.ProcessoParteService;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.FaturamentoSiscomexVO;
import com.portoitapoa.faturamentofast.vo.InformacoesBLVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

/**
 * @author Fernando de Lima on 02/07/20
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaturamentoSiscomexController implements IFaturamentoSiscomexController {

    private final FaturamentoSiscomexService service;
    private final FaturamentoLoteService faturamentoLoteService;
    private final ProcessoParteService processoParteService;
    private final CabotagemImpoClient client;
    private final ManagerClient managerClient;

    @Override
    public ResponseEntity<Page<FaturamentoSiscomexVO>> listAll(final Pageable pageable) {
        log.info("{} Chamada endpoint para listar todos faturamentos siscomex", Util.LOG_PREFIX);

        final Page<FaturamentoSiscomexVO> faturamentos = service.findAll(pageable)
            .map(FaturamentoSiscomexVO::parse);

        return ResponseEntity.ok(faturamentos);
    }

    @Override
    public ResponseEntity<FaturamentoSiscomexVO> findFaturamentoImpo(final String numeroBl) {
        log.info("{} Chamada endpoint para listar faturamentos siscomex de importação", Util.LOG_PREFIX);

        Pageable pageable = PageRequest.ofSize(1);

        //Verifica se numeroBl é uma Cabotagem Importacao
        var retorno = client.filter(numeroBl, pageable);
        if(Objects.nonNull(retorno)){
           return ResponseEntity.ok(FaturamentoSiscomexVO.parse(retorno));
        }

        var siscomex = service.findSiscomexByNumeroBl(numeroBl);
        if(Objects.nonNull(siscomex)){
           return  ResponseEntity.ok(FaturamentoSiscomexVO.parse(siscomex));
        }

        var process = managerClient.findFinishedProcessByBl(numeroBl);
        if(Objects.nonNull(process)){
            return ResponseEntity.ok(FaturamentoSiscomexVO.parse(process));
        }

        log.warn("{} Não encontrado faturamento siscomex/cabotagem para BL: {}", Util.LOG_PREFIX, numeroBl);
        throw new ResourceNotFoundException(EnTagException.NOT_FOUND_SISCOMEX_CABOTAGEM_FAT_BL);
    }

    @Override
    public ResponseEntity<FaturamentoSiscomexVO> postFaturamentoImpo(final String numeroBl) {
        log.info("{} Chamada endpoint para post faturamento Importação", Util.LOG_PREFIX);

        return ResponseEntity.ok(service.postFaturamentoImportacao(numeroBl, false));
    }

    @Override
    public ResponseEntity<FaturamentoSiscomexVO> postFaturamentoImpoFront(final String numeroBl) {
        log.info("{} Chamada endpoint para post faturamento Importação", Util.LOG_PREFIX);

        return ResponseEntity.ok(service.postFaturamentoImportacao(numeroBl, true));
    }

    @Override
    public ResponseEntity<List<String>> postFaturamentoImpoLote(final Integer qtdHoras, Integer delayMinutos) {
        log.info("{} Chamada endpoint para post faturamento Importação em Lote", Util.LOG_PREFIX);

        return ResponseEntity.ok(faturamentoLoteService.postFaturamentoSiscomexImpoLote(qtdHoras, delayMinutos));
    }

    @Override
    public ResponseEntity<List<String>> faturamentoProcessoParteLote(Integer qtdHoras, Integer delayMinutos) {
        log.info("{} Chamada endpoint para gerar faturamento parte em lote", Util.LOG_PREFIX);
        return ResponseEntity.ok(processoParteService.gerarFaturamentos(qtdHoras, delayMinutos));
    }

    @Override
    public ResponseEntity<List<String>> postFaturamentoImpoCrossDockingLote(final Integer qtdDias) {
        log.info("{} Chamada endpoint para post faturamento Importação com crossdocking em Lote", Util.LOG_PREFIX);

        return ResponseEntity.ok(faturamentoLoteService.postFaturamentoSiscomexImpoCrossDockingLote(qtdDias));
    }

    @Override
    public ResponseEntity<FaturamentoSiscomexVO> putAtualizarFaturamentoSiscomex(
            @PathVariable final Integer id,
            @RequestParam(value = "crossdocking") final boolean crossdocking) {
        log.info("{} Chamada endpoint para put de atualização do crossdocking. SISCOMEX: {}, CROSSDOCKING: {}", Util.LOG_PREFIX,
                id, crossdocking);
        final FaturamentoSiscomexVO faturamento = FaturamentoSiscomexVO.parse(service.putAtualizarFaturamentoSiscomex(id, crossdocking));
        return ResponseEntity.ok(faturamento);
    }

    @Override
    public ResponseEntity<Boolean> buscarCrossDocking(final Integer id) {
        log.info("{} Chamada endpoint para buscar o status do crossdocking para o faturamento siscomex: {}", Util.LOG_PREFIX, id);

        return ResponseEntity.ok(service.buscarCrossDocking(id));

    }

    @Override
    public ResponseEntity<List<InformacoesBLVO>> buscarInfoN4(String bl, String di, String ce) {
        return ResponseEntity.ok(service.buscarInfoN4(bl, di, ce));
    }

    @Override
    public ResponseEntity<List<String>> postFaturamentoImpoLoteDeparted(Integer qtdHoras) {
        return ResponseEntity.ok(service.processarImpoLoteDeparted(qtdHoras));
    }
}
