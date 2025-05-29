package com.portoitapoa.faturamentofast.entity;

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
 * @author Weliton Villain
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "vw_msFaturamento_Exprt_Cross_Yard")
public class ExportacaoYard {

    @Id
    @Column(name = "unit_gkey", columnDefinition = "bigint")
    private BigInteger gkey;

    @Column(name = "acv_gkey", columnDefinition = "bigint")
    private BigInteger acvGkey;

    @Column(name = "vessel_visit", columnDefinition = "nvarchar(30)")
    private String navio;

    @Column(name = "ats")
    private LocalDateTime ats;

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

    @Column(name = "dt_liberacao_fat")
    private LocalDateTime dataLiberacaoFaturamento;

    @Column(name = "gkeyEvento", columnDefinition = "bigint")
    private BigInteger gkeyEvento;

    @Column(name = "equipment_id", columnDefinition = "nvarchar(30)")
    private String container;

    @Column(name = "commodity_id", columnDefinition = "nvarchar(255)")
    private String ncm;

    @Transient
    @Builder.Default
    private List<Long> gkeyServicosExcecaoCockPit = new ArrayList<>();

}