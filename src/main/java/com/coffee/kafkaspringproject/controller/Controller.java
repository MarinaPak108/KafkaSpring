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
