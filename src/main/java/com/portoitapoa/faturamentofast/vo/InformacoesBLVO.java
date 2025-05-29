package com.portoitapoa.faturamentofast.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.portoitapoa.faturamentofast.enuns.EnTipoProcessoFaturamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 *
 * @author Alan Lopes
 *
 */
@Schema(name = "Informacoes BL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class InformacoesBLVO {

    @Schema(accessMode = READ_ONLY, example = "19252696", description = "Número do Processo")
    private Long gkey;

    @Schema(accessMode = READ_ONLY,  description = "Eventos")
    private List<EventoVO> chargeableUnitEvent;
}
