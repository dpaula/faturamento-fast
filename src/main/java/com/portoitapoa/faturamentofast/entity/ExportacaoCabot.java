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

/**
 * @author Dionízio Inácio
 */
@Data
@Entity(name = "vw_msFaturamento_Exprt_Cabot")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExportacaoCabot {


    @Id
    @Column(name = "unit_gkey", columnDefinition = "bigint")
    private BigInteger gkey;

    @Column(name = "acv_gkey", columnDefinition = "bigint")
    private BigInteger acvGkey;

    @Column(name = "vessel_visit", columnDefinition = "nvarchar(30)")
    private String navio;

    @Column(name = "dt_liberacao_fat")
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

    @Column(name = "is_cabot")
    private Boolean isCabot;

    @Column(name = "cnpj_fat_cabotagem", columnDefinition = "nvarchar(100)")
    private String cnpjFatCabotagem;

    @Column(name = "cliente_cabotagem", columnDefinition = "nvarchar(80)")
    private String clienteCabotagem;

    @Column(name = "faturarBooking_cabot")
    private Boolean faturarBookingCabotagem;

    @Transient
    @Builder.Default
    private List<Long> gkeyServicosExcecaoCockPit = new ArrayList<>();

    public boolean isFaturarPorBooking() {
        return faturarBooking != null && faturarBooking;
    }

    public boolean isFaturarPorBookingSemCabotagem() {
        return isFaturarPorBooking() && !isCabot();
    }

    public boolean isfaturarBookingCabotagem() {
        return faturarBookingCabotagem != null && faturarBookingCabotagem;
    }

    public boolean isfaturarBookingComCabotagem() {
        return isfaturarBookingCabotagem() && isCabot();
    }

    public boolean isCabot() {
        return isCabot != null && isCabot;
    }

    public String getRadicalCnpj() {
        return Util.getRadicalCnpj(cnpjExportador);
    }

    public String getRadicalcnpjFatCabotagem() {
        return Util.getRadicalCnpj(cnpjFatCabotagem);
    }
}