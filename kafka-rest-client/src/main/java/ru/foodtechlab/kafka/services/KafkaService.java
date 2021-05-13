package ru.foodtechlab.kafka.services;

import lombok.RequiredArgsConstructor;
import ru.foodtechlab.kafka.exception.FailedToFetchTopicsException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.apache.kafka.clients.admin.AdminClient.create;

@Service
@RequiredArgsConstructor
public class KafkaService {
    public static final String INTERNAL_CONSUMER_PREFIX = "__";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapServers;
    @Value("${spring.kafka.ssl.key-password}")
    public String keyPassword;
    @Value("${spring.kafka.ssl.keystore-location}")
    public String keyStoreLocation;
    @Value("${spring.kafka.ssl.keystore-password}")
    public String keyStorePassword;
    @Value("${spring.kafka.ssl.truststore-location}")
    public String trustStoreLocation;
    @Value("${spring.kafka.ssl.truststore-password}")
    public String trustStorePassword;

    public Set<String> getKafkaTopics() throws FailedToFetchTopicsException {
        Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", trustStoreLocation);
        props.put("ssl.truststore.password", trustStorePassword);

        props.put("ssl.key.password", keyPassword);
        props.put("ssl.keystore.password", keyStorePassword);
        props.put("ssl.keystore.location", keyStoreLocation);
        AdminClient client = create(props);
        try {
            return getNonInternalKafkaTopics(client.listTopics(new ListTopicsOptions()).names().get());
        }catch (Exception exception){
            throw new FailedToFetchTopicsException("Failed to fetch kafka topics.", exception);
        }finally {
            client.close();
        }
    }

    public void sendEventToTopic(String topic,String key,String event) {
        kafkaTemplate.send(topic,key, event);
    }

    private Set<String> getNonInternalKafkaTopics(Set<String> topics) {
        return topics
                .stream()
                .filter(KafkaService::nonInternalKafkaTopic)
                .collect(toSet());
    }

    private static boolean nonInternalKafkaTopic(String s) {
        return !s.startsWith(INTERNAL_CONSUMER_PREFIX);
    }

}
