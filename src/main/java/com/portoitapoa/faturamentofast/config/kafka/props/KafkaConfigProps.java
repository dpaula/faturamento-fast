package com.portoitapoa.faturamentofast.config.kafka.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class KafkaConfigProps {

    @Value("${kafka.topics.pre-register-imp.name}")
    private String preRegisterImpTopicName;

    @Value("${kafka.topics.pre-register-exp.name}")
    private String preRegisterExpTopicName;

    @Value("${kafka.topics.cabotagem-import.name}")
    private String cabotagemImportTopicName;

    @Value("${kafka.default-partitions}")
    private int defaultPartitions;

    @Value("${kafka.default-replication-factor}")
    private short defaultReplicationFactor;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;


    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

}
