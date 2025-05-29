package com.portoitapoa.faturamentofast.vo.n4.vo;

import com.portoitapoa.faturamentofast.vo.n4.model.N4Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <Alexsandro Andre> on 06/05/2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class N4ParameterVO {

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, example = "internalDeliveryNumberValue", description = "Nome do id ou nome do Groovy serviço do N4")
    @NotEmpty
    private String id;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, example = "2001699785", description = "Valor a ser pesquisado ou inserido no Groovy serviço do N4")
    @NotEmpty
    private String value;

    public static N4ParameterVO parse(final N4Parameter n4Parameter) {
        return N4ParameterVO.builder()
            .id(n4Parameter.getId())
            .value(n4Parameter.getValue())
            .build();
    }

    public static List<N4ParameterVO> parseAll(final List<N4Parameter> n4Parameters) {
        return n4Parameters.stream()
            .map(N4ParameterVO::parse)
            .collect(Collectors.toList());
    }
}
