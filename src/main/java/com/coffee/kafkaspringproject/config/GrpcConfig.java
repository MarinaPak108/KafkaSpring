package com.coffee.kafkaspringproject.config;

import com.coffee.kafkaspringproject.RoastingResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    private static final Logger logger = LoggerFactory.getLogger(GrpcConfig.class);

    @Bean
    public StreamObserver<RoastingResponse> roastingResponseObserver() {
        return new StreamObserver<RoastingResponse>() {
            @Override
            public void onNext(RoastingResponse value) {
                // Log the response message
                logger.info("Received RoastingResponse: {}", value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                // Log the error message
                logger.error("Error occurred while processing RoastingResponse", t);
            }

            @Override
            public void onCompleted() {
                // Log the completion of the response stream
                logger.info("RoastingResponse stream completed.");
            }
        };
    }
}
