package com.portoitapoa.faturamentofast.vo;

import com.portoitapoa.faturamentofast.entity.Exportacao;
import com.portoitapoa.faturamentofast.entity.ExportacaoCabot;
import com.portoitapoa.faturamentofast.entity.ExportacaoYard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 * @author Fernando de Lima
 */
@Schema(name = "FaturamentoExportacao")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ExportacaoVO {

    @Schema(accessMode = READ_ONLY, example = "11525475", description = "Gkey do processo")
    private BigInteger gkey;

    @Schema(accessMode = READ_ONLY, example = "2175233", description = "Gkey ACV")
    private BigInteger acvGkey;

    @Schema(accessMode = READ_ONLY, example = "VSSEL13", description = "Identificação do navio")
    private String navio;

    @Schema(accessMode = READ_ONLY, example = "2021-02-22T00:00:00", description = "Data ATS")
    private LocalDateTime dataFaturamento;

    @Schema(accessMode = READ_ONLY, example = "ZIMUITJ8021858", description = "Booking do processo")
    private String booking;

    @Schema(accessMode = READ_ONLY, example = "19BR0017632578", description = "DUE do processo")
    private String due;

    @Schema(accessMode = READ_ONLY, example = "00105229000271", description = "CNPJ do Exportador")
    private String cnpjExportador;

    @Schema(accessMode = READ_ONLY, example = "DURLICOUROS IND E COM DE COUROS, EXP E IMPORTACAO LTDA", description = "Razão Social do Exportador")
    private String nomeExportador;

    @Schema(accessMode = READ_ONLY, example = "TRUE", description = "Se é para fatuar por booking")
    private Boolean faturarBooking;

    @Schema(accessMode = READ_ONLY, example = "TRUE", description = "Identifica uma cabotagem")
    private Boolean isCabot;

    @Schema(accessMode = READ_ONLY, example = "TRUE", description = "Identifica um container yard")
    private Boolean yard;

    @Schema(accessMode = READ_ONLY, example = "11525475", description = "Gkey da CUE")
    private BigInteger gkeyEvento;

    @Schema(accessMode = READ_ONLY, example = "PIDU4289912", description = "Container do processo")
    private String container;

    @Schema(accessMode = READ_ONLY, example = "4709", description = "NCM do processo")
    private Long ncm;

    public boolean isFaturarPorBooking() {
        return faturarBooking != null && faturarBooking;
    }

    public static ExportacaoVO parse(final Exportacao expo) {
        return ExportacaoVO.builder()
            .gkey(expo.getGkey())
            .acvGkey(expo.getAcvGkey())
            .navio(expo.getNavio())
            .dataFaturamento(expo.getDataFaturamento())
            .booking(expo.getBooking())
            .due(expo.getDue())
            .cnpjExportador(expo.getCnpjExportador())
            .nomeExportador(expo.getNomeExportador())
            .faturarBooking(expo.getFaturarBooking())
            .build();
    }

    public static ExportacaoVO parseFromCabotagem(final ExportacaoCabot expo) {
        return ExportacaoVO.builder()
                .gkey(expo.getGkey())
                .acvGkey(expo.getAcvGkey())
                .navio(expo.getNavio())
                .dataFaturamento(expo.getDataFaturamento())
                .booking(expo.getBooking())
                .due(expo.getDue())
                .cnpjExportador(expo.getCnpjExportador())
                .nomeExportador(expo.getNomeExportador())
                .faturarBooking(expo.getFaturarBooking())
                .isCabot(true)
                .yard(false)
                .build();
    }

    public static ExportacaoVO parseFromYard(final ExportacaoYard expo) {
        return ExportacaoVO.builder()
                .gkey(expo.getGkey())
                .acvGkey(expo.getAcvGkey())
                .navio(expo.getNavio())
                .dataFaturamento(expo.getDataLiberacaoFaturamento())
                .booking(expo.getBooking())
                .due(expo.getDue())
                .cnpjExportador(expo.getCnpjExportador())
                .nomeExportador(expo.getNomeExportador())
                .faturarBooking(expo.getFaturarBooking())
                .isCabot(false)
                .yard(true)
                .gkeyEvento(expo.getGkeyEvento())
                .container(expo.getContainer())
                .ncm(getNcmTratado(expo.getNcm()))
                .build();
    }

    private static Long getNcmTratado(String ncm) {
        if (ncm == null) {
            return null;
        }

        // ncm pode vir assim: "4709" ou "CELULOSE KRAFT FARDO 02 ARAMES CW"
        // Se for o primeiro caso, retorna o ncm convertido para Long
        if (ncm.matches("\\d+")) {
            return Long.valueOf(ncm);
        }
        // Se for o segundo caso, retorna null
        return null;
    }
}
