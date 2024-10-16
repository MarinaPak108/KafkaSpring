package com.coffee.kafkaspringproject.controller;

import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.listener.CoffeeBagListener;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private CoffeeBagListener coffeeListener;

    @GetMapping("/")
    String home(){
        CoffeeBagEntity mockCoffeeBag = new CoffeeBagEntity("Brazil", 60, 70, 30);

        // Simulate a Kafka ConsumerRecord with the mock CoffeeBag
        ConsumerRecord<String, CoffeeBagEntity> record = new ConsumerRecord<>(
                "new-coffee-bag", 0, 0L, "key1", mockCoffeeBag
        );

        coffeeListener.listenNewCoffeeBag(record);

        return ("first check");
    }
}
