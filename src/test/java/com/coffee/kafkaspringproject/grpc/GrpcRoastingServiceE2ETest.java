package com.coffee.kafkaspringproject.grpc;

import com.coffee.kafkaspringproject.RoastingRequest;
import com.coffee.kafkaspringproject.RoastingResponse;
import com.coffee.kafkaspringproject.RoastingServiceGrpc;
import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import com.coffee.kafkaspringproject.repo.RoastingBatchRepo;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
public class GrpcRoastingServiceE2ETest {

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

        registry.add("grpc.server.inProcessName", () -> "test");
        registry.add("grpc.server.port", () -> "-1");
        registry.add("grpc.client.inProcess.address", () -> "in-process:test");
    }

    @Autowired
    private RoastingBatchRepo roastingBatchRepo;

    @GrpcClient("inProcess")
    private RoastingServiceGrpc.RoastingServiceBlockingStub roastingService;

    @BeforeEach
    void setup() {
        roastingBatchRepo.deleteAll();
    }

    @Test
    void testGrpcToDB() throws Exception {
        // Create a gRPC RoastingRequest
        RoastingRequest request = RoastingRequest.newBuilder()
                .setOriginCountry("Ethiopia")
                .setCoffeeSort(1)
                .setInputWeight(100000)
                .setOutputWeight(90000)
                .setTeamId("123e4567-e89b-12d3-a456-426614174000")
                .build();

        // Call the gRPC method using the injected @GrpcClient
        RoastingResponse response = roastingService.reportRoasting(request);

        // Validate the gRPC response
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        assertThat(response.getMessage()).isEqualTo("Roasting data saved successfully");

        // Await and verify that the roasting batch was saved in the database
        await().atMost(10, SECONDS).untilAsserted(() -> {
            Optional<RoastingBatchEntity> roastingBatchEntity = roastingBatchRepo.findAll().stream().findFirst();
            assertThat(roastingBatchEntity).isPresent();
            assertThat(roastingBatchEntity.get().getOriginCountry()).isEqualTo(request.getOriginCountry());
            assertThat(roastingBatchEntity.get().getUUIDTeamId()).isEqualTo(UUID.fromString(request.getTeamId()));
            assertThat(roastingBatchEntity.get().getInputWeight()).isEqualTo(request.getInputWeight());
            assertThat(roastingBatchEntity.get().getOutputWeight()).isEqualTo(request.getOutputWeight());
            assertThat(roastingBatchEntity.get().getCoffeeSort()).isEqualTo(request.getCoffeeSort());
        });
    }
}
