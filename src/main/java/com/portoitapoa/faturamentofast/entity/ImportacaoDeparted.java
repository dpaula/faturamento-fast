package com.portoitapoa.faturamentofast.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author Alan Lopes
 */
@Data
@Entity(name = "vw_msFaturamento_Imprt_Departed")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportacaoDeparted {

    @Id
    @Column(name = "bl_gkey", columnDefinition = "bigint")
    private BigInteger gkey;

    @Column(name = "numero_bl", columnDefinition = "nvarchar(35)")
    private String numeroBl;

    @Column(nullable = false)
    private Integer servico;

    @Column(name = "canal_distribuicao")
    private String canalDistribuicao;

    @Column(name = "grupo_material")
    private String grupoMaterial;

    @Column(name = "pagador", columnDefinition = "nvarchar(255)")
    private String cnpjPagador;

    @Column(name = "data_armazen")
    private String dataArmazen;

    @Column(name = "cliente_final", columnDefinition = "nvarchar(255)")
    private String cnpjClienteFinal;

    @Column(name = "customer_referencia_2")
    private String customerReferenciaObs;

    @Column(nullable = false)
    private Integer fase;

    @Column(nullable = false)
    private Integer status;

    private String processo;

    @Column(name = "data_insercao")
    private LocalDateTime dataInsercao;

    @Column(name = "doc_aduaneiro_tipo", columnDefinition = "nvarchar(255)")
    private String tipo;

}