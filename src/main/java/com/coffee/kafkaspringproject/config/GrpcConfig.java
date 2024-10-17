package com.coffee.kafkaspringproject.config;

import com.coffee.kafkaspringproject.RoastingResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {
    @Bean
    public StreamObserver<RoastingResponse> roastingResponseObserver() {
        return new StreamObserver<RoastingResponse>() {
            @Override
            public void onNext(RoastingResponse value) {
                // Handle the response, e.g., log or save to a database
                System.out.println("Received response: " + value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                // Handle error
                System.err.println("Error occurred: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                // Handle completion
                System.out.println("Response stream completed.");
            }
        };
    }
}
