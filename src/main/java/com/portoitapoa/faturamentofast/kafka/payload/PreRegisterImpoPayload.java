package com.portoitapoa.faturamentofast.kafka.payload;

import com.portoitapoa.faturamentofast.vo.FaturamentoSiscomexVO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PreRegisterImpoPayload {

    boolean isRealUser;
    FaturamentoSiscomexVO invoicing;

}
