package com.coffee.kafkaspringproject.kafka;

import com.coffee.kafkaspringproject.dto.event.CoffeeBagEventDTO;
import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.kafka.consumer.auto-offset-reset=earliest"  // Start reading from the earliest offset
        }
)
@Testcontainers
public class CoffeeKafkaTest {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @BeforeEach
    void checkListeners() {
        // Ensure that the Kafka listeners are running and subscribed to the topic
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("new-coffee-bag-id");
        assertThat(listenerContainer).isNotNull();
        assertThat(listenerContainer.isRunning()).isTrue();
    }

    @Autowired
    private KafkaAdmin kafkaAdmin;

    // Define the topic creation configuration
    @BeforeEach
    void ensureKafkaIsReady() {
        NewTopic newCoffeeBagTopic = TopicBuilder.name("new-coffee-bag")
                .partitions(1)
                .replicas(1)
                .build();
        kafkaAdmin.createOrModifyTopics(newCoffeeBagTopic);  // Ensure the topic is created
    }

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
    );

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CoffeeBagRepo coffeeBagRepo;

    @BeforeEach
    void setUp() {
        coffeeBagRepo.deleteAll();  // Clear the database before each test
    }

    @Test
    void shouldRecieveViaListenerAndSaveToDB() {
        CoffeeBagEventDTO coffeeBag1 = new CoffeeBagEventDTO("Colombia", 65, 35, 2,1);

        // Send the message to the Kafka topic "new-coffee-bag"
        kafkaTemplate.send("new-coffee-bag", coffeeBag1);
        kafkaTemplate.flush();  // Ensure all messages are sent

        // Await for the message to be processed by the listener and saved in the database
        await()
                .pollInterval(Duration.ofSeconds(3))  // Poll interval between checks
                .atMost(10, SECONDS)                  // Maximum wait time of 10 seconds
                .untilAsserted(() -> {
                    // Check if the message is saved in the database
                    Optional<CoffeeBagEntity> optionalProduct = coffeeBagRepo.findCoffeeBagEntityByOriginCountryAndArabicaPercentageAndRobustaPercentageAndWeightLeftAndCoffeeSort(
                            coffeeBag1.getOriginCountry(),
                            coffeeBag1.getArabicaPercentage(),
                            coffeeBag1.getRobustaPercentage(),
                            coffeeBag1.getWeightLeft(),
                            coffeeBag1.getCoffeeSort()
                    );

                    // Ensure that the record is found
                    assertThat(optionalProduct).isPresent();

                    // Verify that the weight matches the expected value
                    assertThat(optionalProduct.get().getWeightLeft()).isEqualTo(coffeeBag1.getWeightInGr());
                });
    }
}
