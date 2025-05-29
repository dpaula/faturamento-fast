package com.portoitapoa.faturamentofast.model;

import lombok.*;

/**
 * @author Adronilson Junge
 */

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ProcessaFaturamentoExportacao {

    private String cnpjExportador;
    private String navio;
    private String booking;
    private Boolean criarExcecaoPortal = false;

}
