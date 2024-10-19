package com.coffee.kafkaspringproject.listener;

import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CoffeeBagListener {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeBagListener.class);
    private final CoffeeBagRepo coffeeBagRepo;

    public CoffeeBagListener(CoffeeBagRepo coffeeBagRepo) {
        this.coffeeBagRepo = coffeeBagRepo;
    }

    @KafkaListener(topics = "new-coffee-bag", groupId = "coffee-group")
    public void listenNewCoffeeBag(ConsumerRecord<String, CoffeeBagEntity> record) {
        logger.info(
                "Received a consumer record with coffee origin country:{}: ",
                record.value().getOriginCountry()
        );
        CoffeeBagEntity newBag = record.value();
        try {
            coffeeBagRepo.save(newBag);  // Save new coffee bag to the database
            logger.info("Saved new coffee bag: {}", newBag);
        } catch (Exception e) {
            logger.error("Failed to save coffee bag: {}", newBag, e);
        }
    }
}
