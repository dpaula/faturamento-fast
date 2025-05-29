package com.portoitapoa.faturamentofast.config.kafka;


import com.portoitapoa.faturamentofast.util.Util;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.security.core.Authentication;

import java.util.Map;

public class KafkaProducerInterceptor implements ProducerInterceptor<Object, Object> {

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public ProducerRecord<Object, Object> onSend(ProducerRecord<Object, Object> record) {
        final Authentication usuarioCorrente = Util.getAuthentication();
        final Object token = usuarioCorrente.getDetails();
        record.headers().add(new RecordHeader("Authorization", ("Bearer "+token.toString()).getBytes()));
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }

}