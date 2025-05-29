package com.portoitapoa.faturamentofast.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 * @author Fernando de Lima
 */
@Schema(name = "Cliente")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ClienteVO {

    @Schema(accessMode = READ_ONLY, example = "COMISSARIA DE DESPACHOS NELSON SEARA HEUSI LTDA", description = "Nome do cliente")
    private String nome;

    @Schema(accessMode = READ_ONLY, example = "80663958000100", description = "CNPJ do cliente")
    private String cnpj;
}
