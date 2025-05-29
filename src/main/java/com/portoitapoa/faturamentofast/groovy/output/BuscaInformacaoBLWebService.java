package com.portoitapoa.faturamentofast.groovy.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portoitapoa.faturamentofast.vo.InformacoesBLVO;
import com.portoitapoa.faturamentofast.vo.n4.model.N4Return;
import lombok.*;

import java.util.List;


/**
 * {
 *     "gkey" : 19252696,
 *     "chargeableUnitEvent" : [ {
 *       "bexuGkey" : 16621069,
 *       "bexuIbId" : "KOPAH0036E"
 *     } ]
 *   },
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuscaInformacaoBLWebService extends N4Return {

    @JsonProperty("parameter")
    private List<InformacoesBLVO> parameter;

}
