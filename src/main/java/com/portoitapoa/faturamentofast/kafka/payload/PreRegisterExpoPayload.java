package com.portoitapoa.faturamentofast.kafka.payload;

import com.portoitapoa.faturamentofast.vo.FaturamentoExportacaoVO;
import com.portoitapoa.faturamentofast.vo.FaturamentoSiscomexVO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PreRegisterExpoPayload {

    boolean isRealUser;
    FaturamentoExportacaoVO invoicing;

}
