package com.portoitapoa.faturamentofast.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.portoitapoa.faturamentofast.enuns.EnTipoProcessoFaturamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 *
 * @author Fernando de Lima
 *
 */
@Schema(name = "Processo")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(Include.NON_NULL)
public class ProcessoVO {

    @Schema(accessMode = READ_ONLY, example = "19BR0023837737", description = "Número do Processo")
    @EqualsAndHashCode.Include
    private String numero;

    @Schema(accessMode = READ_ONLY, example = "DI", description = "Tipo do Processo de Faturamento")
    @Enumerated(EnumType.STRING)
    private EnTipoProcessoFaturamento tipo;
}
