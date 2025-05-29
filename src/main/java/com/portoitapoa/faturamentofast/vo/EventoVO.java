package com.portoitapoa.faturamentofast.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 *
 * @author Alan Lopes
 *
 */
@Schema(name = "Evento VO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EventoVO {

    @Schema(accessMode = READ_ONLY, example = "16621069", description = "gkey do evento")
    private Long bexuGkey;

    @Schema(accessMode = READ_ONLY, example = "KOPAH0036E", description = "Navio")
    private String bexuIbId;

    @Schema(accessMode = READ_ONLY, example = "VIST_SCANNER", description = "Nome do servico realizado")
    private String bexuEventType;
}
