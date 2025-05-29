package com.portoitapoa.faturamentofast.config.kafka;


import com.portoitapoa.faturamentofast.config.kafka.props.KafkaConfigProps;
import com.portoitapoa.faturamentofast.kafka.payload.CabotagemImportPayload;
import com.portoitapoa.faturamentofast.kafka.payload.PreRegisterExpoPayload;
import com.portoitapoa.faturamentofast.kafka.payload.PreRegisterImpoPayload;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka producers within a Spring application context.
 * This class utilizes {@link KafkaConfigProps} to configure and instantiate Kafka producers and templates for various types of payloads.
 * It encapsulates the Kafka producer settings and provides factory methods for creating {@link org.springframework.kafka.core.ProducerFactory}
 * and {@link org.springframework.kafka.core.KafkaTemplate} instances for different topics and payload types.
 * <p>
 * Each KafkaTemplate bean is specifically configured for a certain type of payload and topic,
 * facilitating the sending of messages to the appropriate Kafka topics with the correct payload types.
 * </p>
 *
 * @author Alan Lopes
 */
@Slf4j
@Configuration
public class KafkaProducerConfig {

    private final KafkaConfigProps kafkaConfigProps;

    public KafkaProducerConfig(final KafkaConfigProps kafkaConfigProps) {
        this.kafkaConfigProps = kafkaConfigProps;
    }

    /**
     * Creates a generic {@link ProducerFactory} bean that is responsible for creating Kafka producer instances.
     * The factory is configured with settings such as bootstrap servers and serializer classes for keys and values.
     * This method is generic and can support any type of value as a payload.
     *
     * @param topic The topic for which the producer is being configured. Used for logging purposes.
     * @param <T>   The type of the payload the producer will send.
     * @return A {@link ProducerFactory} instance configured for Kafka producers.
     */
    @Bean
    public <T> ProducerFactory<String, T> producerFactory(String topic) {
        log.info("{} CONFIGURATION OF THE KAFKA PRODUCER TO TOPIC: {}", Util.LOG_PREFIX, topic);
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProps.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaConfigProps.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaConfigProps.getValueSerializer());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaProducerInterceptor.class.getName());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(name = "preRegisterExpoProducer")
    public KafkaTemplate<String, PreRegisterExpoPayload> preRegisterExpoProducer() {
        return new KafkaTemplate<>(producerFactory(kafkaConfigProps.getPreRegisterExpTopicName()));
    }

    @Bean(name = "preRegisterImpoProducer")
    public KafkaTemplate<String, PreRegisterImpoPayload> preRegisterImpoProducer() {
        return new KafkaTemplate<>(producerFactory(kafkaConfigProps.getPreRegisterImpTopicName()));
    }

    @Bean(name = "cabotagemImpoProducer")
    public KafkaTemplate<String, CabotagemImportPayload> cabotagemImpoProducer() {
        return new KafkaTemplate<>(producerFactory(kafkaConfigProps.getCabotagemImportTopicName()));
    }
}
