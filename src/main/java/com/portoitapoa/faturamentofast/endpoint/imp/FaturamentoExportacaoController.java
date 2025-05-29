package com.portoitapoa.faturamentofast.endpoint.imp;

import com.portoitapoa.faturamentofast.endpoint.IFaturamentoExportacaoController;
import com.portoitapoa.faturamentofast.model.FaturamentoExportacao;
import com.portoitapoa.faturamentofast.model.ProcessaFaturamentoExportacao;
import com.portoitapoa.faturamentofast.service.FaturamentoExpoService;
import com.portoitapoa.faturamentofast.service.FaturamentoLoteService;
import com.portoitapoa.faturamentofast.vo.ExportacaoVO;
import com.portoitapoa.faturamentofast.vo.FaturamentoExportacaoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fernando de Lima on 02/07/20
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaturamentoExportacaoController implements IFaturamentoExportacaoController {

    private final FaturamentoExpoService service;
    private final FaturamentoLoteService faturamentoLoteService;

    @Override
    public ResponseEntity<Page<ExportacaoVO>> listAll(
        final String cnpjExportador,
        final String navio,
        final String booking,
        final Pageable pageable) {
        final Page<ExportacaoVO> faturamentos = service.findAll(cnpjExportador, navio, booking, pageable)
            .map(ExportacaoVO::parse);

        return ResponseEntity.ok(faturamentos);
    }

    @Override
    public ResponseEntity<FaturamentoExportacaoVO> findFaturamentosFiltro(
        final String cnpjExportador,
        final String navio,
        final String booking) {

        final FaturamentoExportacao faturamentoExpo = service.buscarProcessoExpo(cnpjExportador, navio, booking);

        return ResponseEntity.ok(FaturamentoExportacaoVO.parse(faturamentoExpo));
    }

    @Override
    public ResponseEntity<String> postFaturamentoFront(ProcessaFaturamentoExportacao processaFaturamentoExportacao) {

        postFaturamento(processaFaturamentoExportacao, true);

        return ResponseEntity.ok("Novo processo enviado para Faturamento (Com Historico de Ação Manual)!");
    }

    @Override
    public ResponseEntity<String> postFaturamento(ProcessaFaturamentoExportacao processaFaturamentoExportacao) {

        postFaturamento(processaFaturamentoExportacao, false);

        return ResponseEntity.ok("Novo processo enviado para Faturamento!");
    }

    private void postFaturamento(ProcessaFaturamentoExportacao processaFaturamentoExportacao, final boolean isRealUser) {
        final FaturamentoExportacao faturamentoExpo = service.buscarProcessoExpo(processaFaturamentoExportacao.getCnpjExportador(), processaFaturamentoExportacao.getNavio(), processaFaturamentoExportacao.getBooking());

        // valida exceções de CNPJ ou Booking
        service.faturamentoExpoComExcecao(faturamentoExpo);

        service.postFaturamentoOnline(faturamentoExpo, processaFaturamentoExportacao.getCriarExcecaoPortal(), isRealUser);
    }

    @Override
    public ResponseEntity<List<String>> postFaturamentosLote(final Integer qtdHoras, final Integer qtdFaturar) {
        return ResponseEntity.ok(faturamentoLoteService.postFaturamentoExpoLote(qtdHoras, qtdFaturar));
    }

    @Override
    public ResponseEntity<List<String>> postFaturamentosLotePorNavio(final Integer qtdHoras, final String navio) {
        return ResponseEntity.ok(faturamentoLoteService.postFaturamentoExpoLotePorNavio(qtdHoras, navio));
    }

    @Override
    public ResponseEntity<FaturamentoExportacao> postFaturamentoExpoLoteYard(final Integer qtdHoras) {
        return ResponseEntity.ok(faturamentoLoteService.postFaturamentoExpoLoteYard(qtdHoras));
    }

    @Override
    public ResponseEntity<List<FaturamentoExportacaoVO>> postFaturamentosLotePrint(final Integer qtdHoras) {

        final var exportacoes = faturamentoLoteService.postFaturamentoExpoLotePrint(qtdHoras);

        return ResponseEntity.ok(exportacoes.stream().map(FaturamentoExportacaoVO::parse).collect(Collectors.toList()));
    }
}
