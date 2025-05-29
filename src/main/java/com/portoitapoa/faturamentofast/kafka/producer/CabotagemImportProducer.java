package com.portoitapoa.faturamentofast.kafka.producer;

import com.portoitapoa.faturamentofast.config.kafka.props.KafkaConfigProps;
import com.portoitapoa.faturamentofast.kafka.payload.CabotagemImportPayload;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CabotagemImportProducer {

    private final KafkaConfigProps kafkaConfigProps;
    private final KafkaTemplate<String, CabotagemImportPayload> cabotagemImportProducer;

    public CabotagemImportProducer(final KafkaConfigProps kafkaConfigProps, final KafkaTemplate<String, CabotagemImportPayload> cabotagemImportProducer) {
        this.kafkaConfigProps = kafkaConfigProps;
        this.cabotagemImportProducer = cabotagemImportProducer;
    }

    public void sendMessage(CabotagemImportPayload payload) {
        log.info("{} KAFKA - Enviando payload: {} para o topico: {}", Util.LOG_PREFIX, payload, kafkaConfigProps.getCabotagemImportTopicName());
        cabotagemImportProducer.send(kafkaConfigProps.getCabotagemImportTopicName(), payload);
    }

}
