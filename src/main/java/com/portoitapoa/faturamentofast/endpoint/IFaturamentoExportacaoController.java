package com.portoitapoa.faturamentofast.endpoint;

import com.portoitapoa.faturamentofast.model.FaturamentoExportacao;
import com.portoitapoa.faturamentofast.model.ProcessaFaturamentoExportacao;
import com.portoitapoa.faturamentofast.vo.ExportacaoVO;
import com.portoitapoa.faturamentofast.vo.FaturamentoExportacaoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Fernando de Lima
 */
@RestController
@RequestMapping("/siscomex/faturamentos/exportacao")
@CrossOrigin(origins = "*")
@Tag(name = "Faturamentos Exportação", description = "Serviços para gerenciamento dos Faturamentos de Exportação")
public interface IFaturamentoExportacaoController {

    @Operation(summary = "Listar Faturamentos Exportação", description = "Consulta todos as Entradas de Exportação")
    @GetMapping
    ResponseEntity<Page<ExportacaoVO>> listAll(
        @RequestParam(value = "cnpjExportador", required = false) String cnpjExportador,
        @RequestParam(value = "navio", required = false) String navio,
        @RequestParam(value = "booking", required = false) String booking,
        @PageableDefault(sort = "dataFaturamento", direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Faturamento Exportação Filtro", description = "Consulta Faturamento Exportação por Cnpj Exportador, Navio e Booking")
    @GetMapping("/filtro")
    ResponseEntity<FaturamentoExportacaoVO> findFaturamentosFiltro(
        @RequestParam(value = "cnpjExportador") String cnpjExportador,
        @RequestParam(value = "navio") String navio,
        @RequestParam(value = "booking") String booking);

    @Operation(summary = "Processar Faturamento Exportação", description = "Iniciar Processo Faturamento Exportação por Cnpj Exportador, Navio e Booking")
    @PostMapping("/processar")
    ResponseEntity<String> postFaturamento(@RequestBody ProcessaFaturamentoExportacao processaFaturamentoExportacao);

    @Operation(summary = "Processar Faturamento Exportação com historico de ação", description = "Iniciar Processo Faturamento Exportação por Cnpj Exportador, Navio e Booking com historico de ação manual")
    @PostMapping("/processar-front")
    ResponseEntity<String> postFaturamentoFront(@RequestBody ProcessaFaturamentoExportacao processaFaturamentoExportacao);

    @Operation(summary = "Processar Faturamento Exportação Lote", description = "Iniciar Processo Faturamento Exportação em Lote")
    @PostMapping("/processar-lote")
    ResponseEntity<List<String>> postFaturamentosLote(
        @RequestParam(required = false, value = "qtdHoras") Integer qtdHoras, @RequestParam(required = false, value = "qtdFaturar") Integer qtdFaturar);

    @Operation(summary = "Buscar Faturamento Exportação Lote", description = "Busca Processos Faturamento Exportação em Lote")
    @GetMapping("/processar-lote-print")
    ResponseEntity<List<FaturamentoExportacaoVO>> postFaturamentosLotePrint(
        @RequestParam(required = false, value = "qtdHoras") Integer qtdHoras);

    @Operation(summary = "Buscar Faturamento Exportação Lote do tipo YARD", description = "Busca Processos Faturamento Exportação em Lote do tipo YARD")
    @GetMapping("/processar-lote-yard")
    ResponseEntity<FaturamentoExportacao> postFaturamentoExpoLoteYard(
            @RequestParam(value = "qtdHoras") Integer qtdHoras);

    @Operation(summary = "Processar Faturamento Exportação Lote por Navio", description = "Iniciar Processo Faturamento Exportação em Lote por Navio")
    @PostMapping("/processar-lote-navio")
    ResponseEntity<List<String>> postFaturamentosLotePorNavio(
            @RequestParam(value = "qtdHoras") Integer qtdHoras, @RequestParam(value = "navio") String navio);

}