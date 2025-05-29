package com.portoitapoa.faturamentofast.kafka.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.portoitapoa.faturamentofast.model.Solicitacao;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CabotagemImportPayload {

    boolean realUse = true;
    Solicitacao solicitacao;

}
