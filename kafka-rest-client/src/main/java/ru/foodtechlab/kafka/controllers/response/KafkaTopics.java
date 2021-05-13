package ru.foodtechlab.kafka.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class KafkaTopics {
    private Set<String> topics;
}
