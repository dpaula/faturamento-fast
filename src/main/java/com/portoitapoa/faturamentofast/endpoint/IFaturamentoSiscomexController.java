package com.portoitapoa.faturamentofast.endpoint;

import com.portoitapoa.faturamentofast.vo.FaturamentoSiscomexVO;
import com.portoitapoa.faturamentofast.vo.InformacoesBLVO;
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
@RequestMapping("/siscomex/faturamentos")
@CrossOrigin(origins = "*")
@Tag(name = "Faturamentos", description = "Serviços para gerenciamento dos Faturamentos de Entrada Siscomex")
public interface IFaturamentoSiscomexController {

    @Operation(summary = "Listar Faturamentos Siscomex", description = "Consulta todos as Entradas Siscomex")
    @GetMapping
    ResponseEntity<Page<FaturamentoSiscomexVO>> listAll(
        @PageableDefault(sort = "dataLiberacao", direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Faturamento Importacao BL", description = "Consulta Faturamento Importação Siscomex pelo numero BL")
    @GetMapping("/por-bl")
    ResponseEntity<FaturamentoSiscomexVO> findFaturamentoImpo(@RequestParam(value = "numeroBl") String numeroBl);

    @Operation(summary = "Processar Faturamento Importacao BL", description = "Iniciar Processo Faturamento Importação Siscomex pelo numero BL")
    @PostMapping("/processar-impo")
    ResponseEntity<FaturamentoSiscomexVO> postFaturamentoImpo(@RequestParam(value = "numeroBl") String numeroBl);

    @Operation(summary = "Processar Faturamento Importacao BL com historico de ação", description = "Iniciar Processo Faturamento Importação Siscomex pelo numero BL com geração de historico de ação manual")
    @PostMapping("/processar-impo-front")
    ResponseEntity<FaturamentoSiscomexVO> postFaturamentoImpoFront(@RequestParam(value = "numeroBl") String numeroBl);

    @Operation(summary = "Processar Faturamento Importacao BL Lote", description = "Iniciar Processo Faturamento Importação Siscomex em Lote")
    @PostMapping("/processar-impo-lote")
    ResponseEntity<List<String>> postFaturamentoImpoLote(
        @RequestParam(required = false, value = "qtdHoras") Integer qtdHoras,
        @RequestParam(required = false, value = "delayMinutos") Integer delay
    );


    @Operation(summary = "Processar Faturamento Importacao - Parte", description = "Iniciar Processo Faturamento Importação Siscomex do tipo Parte em Lote")
    @PostMapping("/processar-impo-parte-lote")
    ResponseEntity<List<String>> faturamentoProcessoParteLote(
            @RequestParam(required = false, value = "qtdHoras") Integer qtdHoras,
            @RequestParam(required = false, value = "delayMinutos") Integer delayMinutos
    );

    @Operation(summary = "Processar Faturamento Importacao BL com CrossDocking Lote ", description = "Iniciar Processo Faturamento Importação Siscomex com CrossDocking em Lote")
    @PostMapping("/processar-impo-lote-crossdocking")
    ResponseEntity<List<String>> postFaturamentoImpoCrossDockingLote(
            @RequestParam(required = false, value = "qtdDias") Integer qtdDias);

    @Operation(summary = "Atualizar Siscomex para o uso do crossdocking", description = "Atualizar Siscomex para o uso do crossdocking")
    @PostMapping("/{id}/atualizar-crossdocking")
    ResponseEntity<FaturamentoSiscomexVO> putAtualizarFaturamentoSiscomex(
            @PathVariable final Integer id,
            @RequestParam(value = "crossdocking") final boolean crossdocking);

    @Operation(summary = "Buscar o status do crossdocking", description = "Buscar o status do crossdocking")
    @GetMapping("/{id}/crossdocking")
    ResponseEntity<Boolean> buscarCrossDocking(@PathVariable final Integer id);

    @Operation(summary = "Buscar informacoes do BL no N4", description = "Buscar informacoes do BL no N4")
    @GetMapping("/buscar-info-n4")
    ResponseEntity<List<InformacoesBLVO>> buscarInfoN4(@RequestParam String bl, @RequestParam(required = false) String di, @RequestParam(required = false) String ce);


    @Operation(summary = "Processar Faturamento Importacao BL DEPARTED", description = "Iniciar Processo Faturamento Importação em Lote DEPARTED")
    @PostMapping("/processar-impo-departed")
    ResponseEntity<List<String>> postFaturamentoImpoLoteDeparted(
            @RequestParam(required = false, value = "qtdHoras") Integer qtdHoras
    );
}