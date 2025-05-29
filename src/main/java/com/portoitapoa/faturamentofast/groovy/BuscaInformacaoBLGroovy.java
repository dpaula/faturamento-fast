package com.portoitapoa.faturamentofast.groovy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.portoitapoa.faturamentofast.vo.n4.builder.GroovyClass;
import com.portoitapoa.faturamentofast.vo.n4.builder.GroovyParameter;
import lombok.Builder;
import lombok.Data;


/**
 * @author Alan Lopes
 */
@Data
@Builder
@GroovyClass(value = "PortalRetornaListChargeableUnitEventByBlAndInternalWebService")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuscaInformacaoBLGroovy {

    @GroovyParameter(value = "ce")
    public String ce;

    @GroovyParameter(value = "bl")
    public String bl;

    @GroovyParameter(value = "internalDeliveryNumberValue")
    public String internalDeliveryNumberValue;

}
