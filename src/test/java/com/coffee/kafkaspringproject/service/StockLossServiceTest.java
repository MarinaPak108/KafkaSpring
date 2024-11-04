package com.coffee.kafkaspringproject.service;
import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
import com.coffee.kafkaspringproject.repo.RoastingBatchRepo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Testcontainers
public class StockLossServiceTest {

    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        //registry.add("spring.jpa.hibernate.ddl-auto", () -> "update"); // Auto-create/update schema
    }

    @Autowired
    private CoffeeBagRepo coffeeBagRepo;

    @Autowired
    private RoastingBatchRepo roastingBatchRepo;

    @Autowired
    private StockLossService stockLossService;

    @BeforeEach
    public void setup() {
        coffeeBagRepo.deleteAll();
        roastingBatchRepo.deleteAll();
    }

    @Test
    public void testUpdateStock_SuccessfulStockUpdate() {
        // Arrange
        CoffeeBagEntity bag1 = new CoffeeBagEntity("TestCountry", 65,35,2,1);
        CoffeeBagEntity bag2 = new CoffeeBagEntity("TestCountry", 50, 50, 5,1);
        coffeeBagRepo.save(bag1);
        coffeeBagRepo.save(bag2);

        RoastingBatchEntity batch = new RoastingBatchEntity("TestCountry",
                1,
                369600,
                420000,
                UUID.fromString("f6e1e90b-3b89-45ed-8497-90a0472d841d")
                ); // input weight matches total

        // Act
        stockLossService.updateStock(batch);

        // Assert
        CoffeeBagEntity updatedBag1 = coffeeBagRepo.findById(bag1.getId()).orElseThrow();
        CoffeeBagEntity updatedBag2 = coffeeBagRepo.findById(bag2.getId()).orElseThrow();

        assertEquals(0, updatedBag1.getWeightLeft()); // bag1 depleted
        assertEquals(0, updatedBag2.getWeightLeft()); // bag2 also depleted
    }

    @Test
    public void testUpdateStock_PartialBatchWeightFulfillment() {
        // Arrange
        CoffeeBagEntity bag1 = new CoffeeBagEntity("TestCountry", 65,35,2,1);
        CoffeeBagEntity bag2 = new CoffeeBagEntity("TestCountry", 50, 50, 5,1);
        coffeeBagRepo.save(bag1);
        coffeeBagRepo.save(bag2);

        RoastingBatchEntity batch = new RoastingBatchEntity("TestCountry",
                1,
                330600,
                380000,
                UUID.fromString("f6e1e90b-3b89-45ed-8497-90a0472d841d")
        ); // input weight is partially fulfilled

        // Act
        stockLossService.updateStock(batch);

        // Assert
        CoffeeBagEntity updatedBag1 = coffeeBagRepo.findById(bag1.getId()).orElseThrow();
        CoffeeBagEntity updatedBag2 = coffeeBagRepo.findById(bag2.getId()).orElseThrow();

        assertEquals(0, updatedBag1.getWeightLeft()); // bag1 depleted
        assertEquals(40000, updatedBag2.getWeightLeft()); // bag2 partially depleted
    }
}
