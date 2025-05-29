package com.portoitapoa.faturamentofast.model;

import com.portoitapoa.faturamentofast.entity.Exportacao;
import com.portoitapoa.faturamentofast.entity.ExportacaoCabot;
import com.portoitapoa.faturamentofast.enuns.EnCategoriaFaturamento;
import com.portoitapoa.faturamentofast.enuns.EnTipoProcessoFaturamento;
import com.portoitapoa.faturamentofast.vo.ClienteVO;
import com.portoitapoa.faturamentofast.vo.ExportacaoVO;
import com.portoitapoa.faturamentofast.vo.ProcessoVO;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Fernando de Lima
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class FaturamentoExportacao {

    private List<ProcessoVO> processos;

    private String navio;

    private EnCategoriaFaturamento categoriaFaturamento;

    private ClienteVO cliente;

    private List<String> bookings;

    private List<Long> gkeyServicosExcecaoCockPit;

    private LocalDateTime dataFaturamento;

    private boolean faturarPorBooking;

    private boolean isCabot;

    private boolean yard;

    private List<Container> containers = new ArrayList<>();

    public static FaturamentoExportacao parseFromExportacao(final Exportacao exportacao) {
        return FaturamentoExportacao.builder()
            .processos(getProcessoDue(exportacao.getDue()))
            .navio(exportacao.getNavio())
            .categoriaFaturamento(EnCategoriaFaturamento.EXPORTACAO)
            .cliente(getCliente(exportacao))
            .bookings(List.of(exportacao.getBooking()))
            .gkeyServicosExcecaoCockPit(
                !exportacao.getGkeyServicosExcecaoCockPit().isEmpty() ?
                    exportacao.getGkeyServicosExcecaoCockPit() :
                    null)
            .dataFaturamento(exportacao.getDataFaturamento())
            .faturarPorBooking(exportacao.isFaturarPorBooking())
            .build();
    }

    public static FaturamentoExportacao parseFromExportacao(final ExportacaoVO exportacao) {
        return FaturamentoExportacao.builder()
                .processos(getProcessoDue(exportacao.getDue()))
                .navio(exportacao.getNavio())
                .categoriaFaturamento(EnCategoriaFaturamento.EXPORTACAO)
                .cliente(getCliente(exportacao))
                .bookings(List.of(exportacao.getBooking()))
                .dataFaturamento(exportacao.getDataFaturamento())
                .faturarPorBooking(exportacao.isFaturarPorBooking())
                .isCabot(Boolean.TRUE.equals(exportacao.getIsCabot()))
                .yard(Boolean.TRUE.equals(exportacao.getYard()))
                .build();
    }

    public static FaturamentoExportacao parseFromExportacaoYard(final ExportacaoVO exportacao, List<Container> containers) {
        return FaturamentoExportacao.builder()
                .processos(getProcessoDue(exportacao.getDue()))
                .navio(exportacao.getNavio())
                .categoriaFaturamento(EnCategoriaFaturamento.EXPORTACAO)
                .cliente(getCliente(exportacao))
                .bookings(List.of(exportacao.getBooking()))
                .dataFaturamento(exportacao.getDataFaturamento())
                .faturarPorBooking(exportacao.isFaturarPorBooking())
                .isCabot(Boolean.TRUE.equals(exportacao.getIsCabot()))
                .yard(Boolean.TRUE.equals(exportacao.getYard()))
                .containers(new ArrayList<>(containers))
                .build();
    }

    public static FaturamentoExportacao parseFromExportacaoCabotagem(final Exportacao exportacao) {
        return FaturamentoExportacao.builder()
            .processos(getProcessoDue(exportacao.getDue()))
            .navio(exportacao.getNavio())
            .categoriaFaturamento(EnCategoriaFaturamento.EXPORTACAO)
            .cliente(getCliente(exportacao))
            .bookings(List.of(exportacao.getBooking()))
            .gkeyServicosExcecaoCockPit(
                !exportacao.getGkeyServicosExcecaoCockPit().isEmpty() ?
                    exportacao.getGkeyServicosExcecaoCockPit() :
                    null)
            .dataFaturamento(exportacao.getDataFaturamento())
            .faturarPorBooking(exportacao.isFaturarPorBooking())
            .build();
    }

    public static FaturamentoExportacao parseFromExportacaoCabotagem(final ExportacaoCabot exportacaoCabot) {
        return FaturamentoExportacao.builder()
            .processos(getProcessoDue(exportacaoCabot.getDue()))
            .navio(exportacaoCabot.getNavio())
            .categoriaFaturamento(EnCategoriaFaturamento.EXPORTACAO)
            .cliente(getClienteCabotagem(exportacaoCabot))
            .bookings(List.of(exportacaoCabot.getBooking()))
            .gkeyServicosExcecaoCockPit(
                !exportacaoCabot.getGkeyServicosExcecaoCockPit().isEmpty() ?
                    exportacaoCabot.getGkeyServicosExcecaoCockPit() :
                    null)
            .dataFaturamento(exportacaoCabot.getDataFaturamento())
            .faturarPorBooking(exportacaoCabot.isfaturarBookingCabotagem())
            .isCabot(exportacaoCabot.isCabot())
            .build();
    }

    private static List<ProcessoVO> getProcessoDue(final String due) {

        final ProcessoVO processoDue = ProcessoVO.builder()
            .numero(due)
            .tipo(EnTipoProcessoFaturamento.DUE)
            .build();

        return List.of(processoDue);
    }

    private static ClienteVO getCliente(final Exportacao exportacao) {
        if (StringUtils.isBlank(exportacao.getCnpjExportador()) && StringUtils.isBlank(
            exportacao.getNomeExportador())) {
            return null;
        }

        return ClienteVO.builder()
            .nome(getNomeCliente(exportacao.getNomeExportador()))
            .cnpj(getCnpjCliente(exportacao.getCnpjExportador()))
            .build();
    }

    private static ClienteVO getCliente(final ExportacaoVO exportacao) {
        if (StringUtils.isBlank(exportacao.getCnpjExportador()) && StringUtils.isBlank(
                exportacao.getNomeExportador())) {
            return null;
        }

        return ClienteVO.builder()
                .nome(getNomeCliente(exportacao.getNomeExportador()))
                .cnpj(getCnpjCliente(exportacao.getCnpjExportador()))
                .build();
    }

    private static ClienteVO getClienteCabotagem(final ExportacaoCabot exportacao) {
        if (StringUtils.isBlank(exportacao.getCnpjFatCabotagem()) && StringUtils.isBlank(
            exportacao.getClienteCabotagem())) {
            return null;
        }

        return ClienteVO.builder()
            .nome(getNomeCliente(exportacao.getClienteCabotagem()))
            .cnpj(getCnpjCliente(exportacao.getCnpjFatCabotagem()))
            .build();
    }

    private static String getCnpjCliente(final String cnpjFaturarPara) {

        if (StringUtils.isBlank(cnpjFaturarPara)) {
            return null;
        }

        return cnpjFaturarPara.trim();
    }

    private static String getNomeCliente(final String nomeFaturarPara) {

        if (StringUtils.isBlank(nomeFaturarPara)) {
            return null;
        }

        return nomeFaturarPara.trim();
    }

    public static FaturamentoExportacao parseFromExportacaoBookigs(final ExportacaoVO exportacao,
        final List<ProcessoVO> processosDue, final List<String> bookings) {

        return FaturamentoExportacao.builder()
            .processos(processosDue)
            .navio(exportacao.getNavio())
            .categoriaFaturamento(EnCategoriaFaturamento.EXPORTACAO)
            .cliente(getCliente(exportacao))
            .bookings(bookings)
            .dataFaturamento(exportacao.getDataFaturamento())
            .faturarPorBooking(exportacao.isFaturarPorBooking())
                .yard(Boolean.TRUE.equals(exportacao.getYard()))
            .build();
    }

    public static List<ProcessoVO> processosDueFromExportacoes(final List<ExportacaoVO> exportacoes) {
        return exportacoes.stream()
            .map(ExportacaoVO::getDue)
            .filter(Objects::nonNull)
            .distinct()
            .map(due -> ProcessoVO.builder()
                .numero(due)
                .tipo(EnTipoProcessoFaturamento.DUE)
                .build())
            .collect(Collectors.toList());
    }

    public static List<String> bookingsFromExportacoes(final List<ExportacaoVO> exportacoes) {
        return exportacoes.stream()
            .map(ExportacaoVO::getBooking)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
    }

    public String getGkeyServicosJoin() {
        if (CollectionUtils.isEmpty(gkeyServicosExcecaoCockPit)) {
            return null;
        }

        final var gkeysStr = gkeyServicosExcecaoCockPit.stream()
            .filter(Objects::nonNull)
            .map(String::valueOf)
            .collect(Collectors.toList());

        return String.join(",", gkeysStr);
    }

    public String getBookingsJoin() {
        if (CollectionUtils.isEmpty(bookings)) {
            return null;
        }
        return String.join(",", bookings);
    }
}
