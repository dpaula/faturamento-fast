package com.portoitapoa.faturamentofast.vo;

import lombok.*;

import java.util.List;

/**
 * @author Fernando de Lima
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class EmailInputSimples {

    private List<String> destinatarios;
    private String titulo;
    private String corpo;
}
