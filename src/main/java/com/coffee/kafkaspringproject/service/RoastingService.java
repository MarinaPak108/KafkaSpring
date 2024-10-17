package com.coffee.kafkaspringproject.service;

import com.coffee.kafkaspringproject.RoastingRequest;
import com.coffee.kafkaspringproject.RoastingResponse;
import com.coffee.kafkaspringproject.RoastingServiceGrpc;
import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import com.coffee.kafkaspringproject.repo.RoastingBatchRepo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.UUID;

@GrpcService
public class RoastingService extends RoastingServiceGrpc.RoastingServiceImplBase {
    private final RoastingBatchRepo roastingBatchRepo;

    public RoastingService(RoastingBatchRepo roastingBatchRepo) {
        this.roastingBatchRepo = roastingBatchRepo;
    }

    @Override
    public void reportRoasting(RoastingRequest request, StreamObserver<RoastingResponse> responseObserver) {
        // Create a new RoastingBatch entity from the request
        RoastingBatchEntity batch = new RoastingBatchEntity();
        batch.setOriginCountry(request.getOriginCountry());
        batch.setCoffeeSort(request.getCoffeeSort());
        batch.setOutputWeight(request.getOutputWeight());
        batch.setTeamId(UUID.fromString(request.getTeamId()));

        // Save the batch to the database
        roastingBatchRepo.save(batch);

        // Build the gRPC response
        RoastingResponse response = RoastingResponse.newBuilder()
                .setMessage("Roasting data saved successfully")
                .build();

        // Send the response and complete the stream
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
