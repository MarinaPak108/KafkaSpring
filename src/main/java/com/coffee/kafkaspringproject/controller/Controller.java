package com.coffee.kafkaspringproject.controller;

import com.coffee.kafkaspringproject.RoastingRequest;
import com.coffee.kafkaspringproject.RoastingResponse;
import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.listener.CoffeeBagListener;
import com.coffee.kafkaspringproject.service.RoastingService;
import com.coffee.kafkaspringproject.service.StockLossService;
import io.grpc.stub.StreamObserver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class Controller {

    @Autowired
    private CoffeeBagListener coffeeListener;

    @Autowired
    private RoastingService roastingService;

    @Autowired
    private StockLossService stockLossService;

    @Autowired
    private StreamObserver<RoastingResponse> responseObserver;

    @GetMapping("/")
    String home(){

        List<CoffeeBagEntity> arrival = new ArrayList<>();
        CoffeeBagEntity coffeeBag1 = new CoffeeBagEntity("Colombia", 65, 35, 2,1);
        arrival.add(coffeeBag1);
        CoffeeBagEntity coffeeBag2 = new CoffeeBagEntity("Colombia", 50, 50, 5,1);
        arrival.add(coffeeBag2);
        CoffeeBagEntity coffeeBag3 = new CoffeeBagEntity("Argentina", 75, 25, 3,2);
        arrival.add(coffeeBag3);
        CoffeeBagEntity coffeeBag4 = new CoffeeBagEntity("Argentina", 45, 55, 7,2);
        arrival.add(coffeeBag4);
        CoffeeBagEntity coffeeBag5 = new CoffeeBagEntity("Brazil", 75, 25, 1,3);
        arrival.add(coffeeBag5);
        CoffeeBagEntity coffeeBag6 = new CoffeeBagEntity("Brazil", 65, 35, 2,3);
        arrival.add(coffeeBag6);
        // Simulate a Kafka ConsumerRecord with the mock CoffeeBag
        for(CoffeeBagEntity bag : arrival){
            ConsumerRecord<String, CoffeeBagEntity> record = new ConsumerRecord<>(
                    "new-coffee-bag", 0, 0L, "key2", bag
            );
            coffeeListener.listenNewCoffeeBag(record);
        }
        return ("first check");
    }

    @GetMapping("/roast")
    String roast(){
        List<RoastingRequest> requests = new ArrayList<>();

        RoastingRequest request1 = RoastingRequest.newBuilder()
                .setOriginCountry("Colombia")
                .setCoffeeSort(1)
                .setOutputWeight(44000)
                .setInputWeight(50000)
                .setTeamId("e77c55d2-7fcb-4b8d-96df-f7da1f23a242")
                .build();
        requests.add(request1);

        RoastingRequest request2 = RoastingRequest.newBuilder()
                .setOriginCountry("Colombia")
                .setCoffeeSort(1)
                .setOutputWeight(98400)
                .setInputWeight(120000)
                .setTeamId("8e674d10-2f19-40cb-9e7d-67e6e3aafc8a")
                .build();
        requests.add(request2);

        RoastingRequest request3 = RoastingRequest.newBuilder()
                .setOriginCountry("Colombia")
                .setCoffeeSort(1)
                .setOutputWeight(65450)
                .setInputWeight(77000)
                .setTeamId("f6e1e90b-3b89-45ed-8497-90a0472d841d")
                .build();
        requests.add(request3);

        RoastingRequest request4 = RoastingRequest.newBuilder()
                .setOriginCountry("Argentina")
                .setCoffeeSort(2)
                .setOutputWeight(270000)
                .setInputWeight(300000)
                .setTeamId("5b2cc2e4-d18d-4eb8-b7a8-27f2b35d3b51")
                .build();
        requests.add(request4);

        RoastingRequest request5 = RoastingRequest.newBuilder()
                .setOriginCountry("Argentina")
                .setCoffeeSort(2)
                .setOutputWeight(30450)
                .setInputWeight(35000)
                .setTeamId("6e9a60ea-38a4-4c58-b293-890d3cc8a00f")
                .build();
        requests.add(request5);

        RoastingRequest request6 = RoastingRequest.newBuilder()
                .setOriginCountry("Argentina")
                .setCoffeeSort(2)
                .setOutputWeight(91300)
                .setInputWeight(110000)
                .setTeamId("e77c55d2-7fcb-4b8d-96df-f7da1f23a242")
                .build();
        requests.add(request6);

        RoastingRequest request7 = RoastingRequest.newBuilder()
                .setOriginCountry("Brazil")
                .setCoffeeSort(3)
                .setOutputWeight(12740)
                .setInputWeight(14000)
                .setTeamId("5b2cc2e4-d18d-4eb8-b7a8-27f2b35d3b51")
                .build();
        requests.add(request7);

        RoastingRequest request8 = RoastingRequest.newBuilder()
                .setOriginCountry("Brazil")
                .setCoffeeSort(3)
                .setOutputWeight(66000)
                .setInputWeight(75000)
                .setTeamId("8e674d10-2f19-40cb-9e7d-67e6e3aafc8a")
                .build();
        requests.add(request8);

        RoastingRequest request9 = RoastingRequest.newBuilder()
                .setOriginCountry("Brazil")
                .setCoffeeSort(3)
                .setOutputWeight(8200)
                .setInputWeight(10000)
                .setTeamId("6e9a60ea-38a4-4c58-b293-890d3cc8a00f")
                .build();
        requests.add(request9);

        for(RoastingRequest request:requests){
            roastingService.reportRoasting(request, responseObserver);
        }
        return "completed";
    }



    @GetMapping("/stock")
    public ResponseEntity<?> getStock(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer coffeeSort) {
        try{
            int sum = stockLossService.getFilteredStock(country, coffeeSort);
            return ResponseEntity.ok(sum);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/loss-percentage")
    public ResponseEntity<?> getLossPercentage(
            @RequestParam(required = false) String teamId,
            @RequestParam(required = false) String country) {
        try{
            double lossPercentage = stockLossService.getLossPercentage(teamId, country);
            return ResponseEntity.ok(lossPercentage);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

}
