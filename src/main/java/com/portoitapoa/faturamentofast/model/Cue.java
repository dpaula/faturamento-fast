package com.portoitapoa.faturamentofast.model;

import com.portoitapoa.faturamentofast.enuns.EnCategoriaCue;
import com.portoitapoa.faturamentofast.vo.ExportacaoVO;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class Cue{

    private Long gkeyUnit;

    @EqualsAndHashCode.Include
    private Long gkeyEvento;
    private Long batchId;
    private String blProcesso;
    private String booking;

    @Enumerated(EnumType.STRING)
    private EnCategoriaCue categoria;
    private Long ncm;
    private String cnpj;
    private String numeroProcesso;
    private BigDecimal cif;
    private String servicoRealizado;
    private String cnpjAdquirente;
    private String cnpjAgenteCarga;
    private String cnpjFaturarPara;
    private String statusServico;
    private String navio;

    public static Cue parse(final ExportacaoVO vo) {
        return Cue.builder()
                .gkeyUnit(vo.getGkey() != null ? vo.getGkey().longValue() : null)
                .gkeyEvento(vo.getGkeyEvento() != null ? vo.getGkeyEvento().longValue() : null)
                .booking(vo.getBooking())
                .categoria(EnCategoriaCue.EXPORTACAO)
                .ncm(vo.getNcm())
                .cnpj(vo.getCnpjExportador())
                .numeroProcesso(vo.getDue())
                .navio(vo.getNavio())
                .build();
    }
}

