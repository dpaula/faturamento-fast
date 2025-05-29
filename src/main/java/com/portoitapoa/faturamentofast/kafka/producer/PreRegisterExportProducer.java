package com.portoitapoa.faturamentofast.kafka.producer;

import com.portoitapoa.faturamentofast.config.kafka.props.KafkaConfigProps;
import com.portoitapoa.faturamentofast.kafka.payload.PreRegisterExpoPayload;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PreRegisterExportProducer {

    private final KafkaConfigProps kafkaConfigProps;
    private final KafkaTemplate<String, PreRegisterExpoPayload> preRegisterExpoProducer;

    public PreRegisterExportProducer(final KafkaConfigProps kafkaConfigProps, final KafkaTemplate<String, PreRegisterExpoPayload> preRegisterExpoProducer) {
        this.kafkaConfigProps = kafkaConfigProps;
        this.preRegisterExpoProducer = preRegisterExpoProducer;
    }

    public void sendMessage(PreRegisterExpoPayload payload) {
        String cid = Util.getCorrelationId();
        log.info("{} [{}] KAFKA - Enviando payload: {} para o topico: {}", Util.LOG_PREFIX, cid, payload, kafkaConfigProps.getPreRegisterExpTopicName());

        var message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, kafkaConfigProps.getPreRegisterExpTopicName())
                .setHeader("correlationId", cid)
                .build();

        preRegisterExpoProducer.send(message);
    }

}
