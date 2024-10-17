package com.coffee.kafkaspringproject.controller;

import com.coffee.kafkaspringproject.RoastingRequest;
import com.coffee.kafkaspringproject.RoastingResponse;
import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.listener.CoffeeBagListener;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
import com.coffee.kafkaspringproject.service.RoastingService;
import io.grpc.stub.StreamObserver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class Controller {

    @Autowired
    private CoffeeBagListener coffeeListener;

    @Autowired
    private RoastingService roastingService;

    @Autowired
    private StreamObserver<RoastingResponse> responseObserver;

    @GetMapping("/")
    String home(){
        CoffeeBagEntity mockCoffeeBag = new CoffeeBagEntity("Arabica", 65, 25, 30, 2);
        // Simulate a Kafka ConsumerRecord with the mock CoffeeBag
        ConsumerRecord<String, CoffeeBagEntity> record = new ConsumerRecord<>(
                "new-coffee-bag", 0, 0L, "key2", mockCoffeeBag
        );
        coffeeListener.listenNewCoffeeBag(record);
        return ("first check");
    }

    @GetMapping("/roast")
    String roast(){
        RoastingRequest request = RoastingRequest.newBuilder()
                .setOriginCountry("Brazil")
                .setCoffeeSort(1)
                .setOutputWeight(100)
                .setTeamId(UUID.randomUUID().toString())
                .build();

        roastingService.reportRoasting(request, responseObserver);
        return  responseObserver.toString();
    }
}
