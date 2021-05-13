package ru.foodtechlab.kafka.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExampleProducer {

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.ssl.key-password}") String keyPassword,
            @Value("${spring.kafka.ssl.keystore-location}") String keyStoreLocation,
            @Value("${spring.kafka.ssl.keystore-password}") String keyStorePassword,
            @Value("${spring.kafka.ssl.truststore-location}") String trustStoreLocation,
            @Value("${spring.kafka.ssl.truststore-password}") String trustStorePassword
    ){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer");
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", trustStoreLocation);
        props.put("ssl.truststore.password", trustStorePassword);

        props.put("ssl.key.password", keyPassword);
        props.put("ssl.keystore.password", keyStorePassword);
        props.put("ssl.keystore.location", keyStoreLocation);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }
}
