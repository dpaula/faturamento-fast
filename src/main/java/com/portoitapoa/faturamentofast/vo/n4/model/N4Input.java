package com.portoitapoa.faturamentofast.vo.n4.model;

import com.portoitapoa.faturamentofast.vo.n4.vo.N4InputVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author <Alexsandro Andre> on 06/05/2021
 */
@AllArgsConstructor
@Data
@Builder
@ToString
public class N4Input {

    private final String className;
    private final List<N4Parameter> n4Parameters;

    public static N4Input parse(final N4InputVO vo) {
        return N4Input.builder()
            .className(vo.getClassName())
            .n4Parameters(N4Parameter.parseAll(vo.getParameters()))
            .build();


    }
}
