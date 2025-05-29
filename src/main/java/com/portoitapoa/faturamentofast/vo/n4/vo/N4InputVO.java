package com.portoitapoa.faturamentofast.vo.n4.vo;

import com.portoitapoa.faturamentofast.vo.n4.model.N4Input;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <Alexsandro Andre> on 06/05/2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class N4InputVO {

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, example = "PortalRetornaListChargeableUnitEventWebService", description = "Nome da classe ou nome do Groovy serviço do N4")
    @NotEmpty
    private String className;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, example = "", description = "Parametros do groovy para chamar o serviço")
    @Builder.Default
    @NotEmpty
    private List<N4ParameterVO> parameters = new ArrayList<>();

    public static N4InputVO parse(final N4Input input) {
        return N4InputVO.builder()
            .className(input.getClassName())
            .parameters(N4ParameterVO.parseAll(input.getN4Parameters()))
            .build();
    }
}
