package com.portoitapoa.faturamentofast.vo.n4.model;

import com.portoitapoa.faturamentofast.vo.n4.vo.N4ParameterVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <Alexsandro Andre> on 06/05/2021
 */
@AllArgsConstructor
@Data
@Builder
@ToString
public class N4Parameter {

    private final String id;
    private final String value;

    public static N4Parameter parse(final N4ParameterVO vo) {
        return N4Parameter.builder()
            .id(vo.getId())
            .value(vo.getValue())
            .build();
    }

    public static List<N4Parameter> parseAll(final List<N4ParameterVO> vos) {
        return vos.stream()
            .map(N4Parameter::parse)
            .collect(Collectors.toList());
    }
}
