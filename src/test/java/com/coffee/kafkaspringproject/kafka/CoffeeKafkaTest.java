package com.coffee.kafkaspringproject.kafka;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import java.time.Duration;
import java.util.Optional;

import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.kafka.consumer.auto-offset-reset=earliest",
                "spring.datasource.url=jdbc:tc:mysql:8.0.32:///db",
        }
)
@Testcontainers
public class CoffeeKafkaTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
    );

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, CoffeeBagEntity> kafkaTemplate;

    @Autowired
    private CoffeeBagRepo coffeeBagRepo;

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldRecieveViaListenerAndSaveToDB() {
        CoffeeBagEntity coffeeBag1 = new CoffeeBagEntity("Colombia", 65, 35, 2,1);

        kafkaTemplate.send("product-price-changes", coffeeBag1);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    Optional<CoffeeBagEntity> optionalProduct = coffeeBagRepo.findCoffeeBagEntityByOriginCountryAndArabicaPercentageAndRobustaPercentageAndWeightLeftAndCoffeeSort(
                            coffeeBag1.getOriginCountry(),
                            coffeeBag1.getArabicaPercentage(),
                            coffeeBag1.getRobustaPercentage(),
                            coffeeBag1.getWeightLeft(),
                            coffeeBag1.getCoffeeSort()
                    );
                    assertThat(optionalProduct).isPresent();
                    assertThat(optionalProduct.get().getWeightLeft()).isEqualTo(coffeeBag1.getWeightInGr());
                });
    }
}
