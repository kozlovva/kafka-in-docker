package ru.foodtechlab.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.HashMap;
import java.util.UUID;


@Configuration
public class ExampleConsumer {

    @Bean
    public ConsumerFactory<String, String> consumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.ssl.key-password}") String keyPassword,
            @Value("${spring.kafka.ssl.keystore-location}") String keyStoreLocation,
            @Value("${spring.kafka.ssl.keystore-password}") String keyStorePassword,
            @Value("${spring.kafka.ssl.truststore-location}") String trustStoreLocation,
            @Value("${spring.kafka.ssl.truststore-password}") String trustStorePassword
    ) {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //SSL Config
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", trustStoreLocation);
        props.put("ssl.truststore.password", trustStorePassword);
        props.put("ssl.key.password", keyPassword);
        props.put("ssl.keystore.password", keyStorePassword);
        props.put("ssl.keystore.location", keyStoreLocation);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    //Add listener
    @KafkaListener(topics = "demo", containerFactory = "kafkaListenerContainerFactory")
    public void demoTopicListener(@Payload String message) {
        System.out.println("Message: " + message);
    }
}
