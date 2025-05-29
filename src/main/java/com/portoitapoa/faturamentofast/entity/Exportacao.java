package com.portoitapoa.faturamentofast.entity;

import com.portoitapoa.faturamentofast.util.Util;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Fernando de Lima
 * @author Dionízio Inácio
 */
@Data
@Entity(name = "vw_msFaturamento_Exprt")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Exportacao {


    @Id
    @Column(name = "unit_gkey", columnDefinition = "bigint")
    private BigInteger gkey;

    @Column(name = "acv_gkey", columnDefinition = "bigint")
    private BigInteger acvGkey;

    @Column(name = "vessel_visit", columnDefinition = "nvarchar(30)")
    private String navio;

    @Column(name = "ats")
    private LocalDateTime dataFaturamento;

    @Column(name = "booking", columnDefinition = "nvarchar(30)")
    private String booking;

    @Column(name = "dde_dse_due", columnDefinition = "nvarchar(255)")
    private String due;

    @Column(name = "cnpj_exportador", columnDefinition = "nvarchar(100)")
    private String cnpjExportador;

    @Column(name = "exportador", columnDefinition = "nvarchar(80)")
    private String nomeExportador;

    @Column(name = "faturarBooking")
    private Boolean faturarBooking;

//    @Transient
//    private Boolean isCabot;

    @Transient
    @Builder.Default
    private List<Long> gkeyServicosExcecaoCockPit = new ArrayList<>();

    public boolean isFaturarPorBooking() {
        return faturarBooking != null && faturarBooking;
    }

  //  public boolean isCabot() {
//        return isCabot != null && isCabot;
//    }

    public String getRadicalCnpj() {
        return Util.getRadicalCnpj(cnpjExportador);
    }

    public static Exportacao parse(final ExportacaoCabot exportacaoCabot) {

        if(Objects.isNull(exportacaoCabot)){
            return null;
        }

        return Exportacao.builder()
            .gkey(exportacaoCabot.getGkey())
            .acvGkey(exportacaoCabot.getAcvGkey())
            .navio(exportacaoCabot.getNavio())
            .dataFaturamento(exportacaoCabot.getDataFaturamento())
            .booking(exportacaoCabot.getBooking())
            .due(exportacaoCabot.getDue())
            .cnpjExportador(exportacaoCabot.getCnpjExportador())
            .nomeExportador(exportacaoCabot.getNomeExportador())
            .faturarBooking(exportacaoCabot.getFaturarBooking())
            .gkeyServicosExcecaoCockPit(exportacaoCabot.getGkeyServicosExcecaoCockPit())
            .build();
    }

}