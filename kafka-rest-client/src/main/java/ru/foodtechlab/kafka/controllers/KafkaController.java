package ru.foodtechlab.kafka.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.foodtechlab.kafka.controllers.response.KafkaTopics;
import ru.foodtechlab.kafka.exception.FailedToFetchTopicsException;
import ru.foodtechlab.kafka.services.KafkaService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaService kafkaService;

    @GetMapping("/topics")
    public KafkaTopics getTopics() throws FailedToFetchTopicsException {
        return new KafkaTopics(kafkaService.getKafkaTopics());
    }

    @PostMapping( value = "/topic/{topicName}", consumes = APPLICATION_JSON_VALUE)
    public void sendJsonEventToTopic(@PathVariable("topicName") String topicName,
                                     @RequestHeader(name = "key") String key,
                                     @RequestBody String payload){
        kafkaService.sendEventToTopic(topicName, key, payload);
    }


}
