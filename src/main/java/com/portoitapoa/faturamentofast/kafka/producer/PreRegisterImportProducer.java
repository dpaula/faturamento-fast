package com.portoitapoa.faturamentofast.kafka.producer;

import com.portoitapoa.faturamentofast.config.kafka.props.KafkaConfigProps;
import com.portoitapoa.faturamentofast.kafka.payload.PreRegisterImpoPayload;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PreRegisterImportProducer {

    private final KafkaConfigProps kafkaConfigProps;
    private final KafkaTemplate<String, PreRegisterImpoPayload> preRegisterImpoProducer;

    public PreRegisterImportProducer(final KafkaConfigProps kafkaConfigProps, final KafkaTemplate<String, PreRegisterImpoPayload> preRegisterImpoProducer) {
        this.kafkaConfigProps = kafkaConfigProps;
        this.preRegisterImpoProducer = preRegisterImpoProducer;
    }

    public void sendMessage(PreRegisterImpoPayload payload) {
        log.info("{} KAFKA - Enviando payload: {} para o topico: {}", Util.LOG_PREFIX, payload, kafkaConfigProps.getPreRegisterImpTopicName());
        preRegisterImpoProducer.send(kafkaConfigProps.getPreRegisterImpTopicName(), payload);
    }

}
