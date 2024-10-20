package com.coffee.kafkaspringproject.listener;


import com.coffee.kafkaspringproject.dto.event.CoffeeBagEventDTO;
import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
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

    @KafkaListener(id = "new-coffee-bag-id", topics = "new-coffee-bag", groupId = "coffee-group")
    public void listenNewCoffeeBag(CoffeeBagEventDTO event) {
        logger.info("Received coffee bag event with origin country: {}", event.getOriginCountry());
        try {
            // Convert CoffeeBagEventDTO to CoffeeBagEntity
            CoffeeBagEntity coffeeBagEntity = new CoffeeBagEntity(
                    event.getOriginCountry(),
                    event.getArabicaPercentage(),
                    event.getRobustaPercentage(),
                    event.getNrOfBags(),
                    event.getCoffeeSort()
            );

            // Save the entity to the database
            coffeeBagRepo.save(coffeeBagEntity);
            logger.info("Saved new coffee bag: {}", coffeeBagEntity);
        } catch (Exception e) {
            logger.error("Failed to save coffee bag event: {}", event, e);
        }
    }
}
