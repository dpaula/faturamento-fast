package com.portoitapoa.faturamentofast.config.kafka;

import com.portoitapoa.faturamentofast.config.kafka.props.KafkaConfigProps;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka topics within a Spring application context.
 * Utilizes {@link KafkaConfigProps} for specifying the Kafka cluster configuration details.
 * This class is responsible for setting up a {@link KafkaAdmin} bean, which is essential
 * for managing Kafka topics and their configurations.
 * <p>
 * The {@code kafkaAdmin} method configures a {@link KafkaAdmin} instance with properties
 * such as the bootstrap servers obtained from {@code kafkaConfigProps}. The {@link KafkaAdmin}
 * is a critical component that allows the application to interact with the Kafka cluster
 * for administrative operations, such as creating and configuring topics.
 * </p>
 *
 * @author Alan Lopes
 */
@Slf4j
@Configuration
public class KafkaTopicConfig {

    private final KafkaConfigProps kafkaConfigProps;

    public KafkaTopicConfig(final KafkaConfigProps kafkaConfigProps) {
        this.kafkaConfigProps = kafkaConfigProps;
    }

    /**
     * Creates a {@link KafkaAdmin} bean that is responsible for managing Kafka topic configurations.
     * The KafkaAdmin is configured with properties such as the Kafka cluster's bootstrap servers,
     * allowing it to perform administrative operations against the Kafka cluster.
     *
     * @return A {@link KafkaAdmin} instance configured with the necessary properties to manage Kafka topics.
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProps.getBootstrapServers());
        return new KafkaAdmin(props);
    }

    @Bean
    public NewTopic preRegisterImpTopic() {
        return new NewTopic(
                kafkaConfigProps.getPreRegisterImpTopicName(),
                kafkaConfigProps.getDefaultPartitions(),
                kafkaConfigProps.getDefaultReplicationFactor()
        );
    }

    @Bean
    public NewTopic preRegisterExpTopic() {
        return new NewTopic(
                kafkaConfigProps.getPreRegisterExpTopicName(),
                kafkaConfigProps.getDefaultPartitions(),
                kafkaConfigProps.getDefaultReplicationFactor()
        );
    }


}
