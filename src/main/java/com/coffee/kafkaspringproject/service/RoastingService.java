package com.coffee.kafkaspringproject.service;

import com.coffee.kafkaspringproject.RoastingRequest;
import com.coffee.kafkaspringproject.RoastingResponse;
import com.coffee.kafkaspringproject.RoastingServiceGrpc;
import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import com.coffee.kafkaspringproject.repo.RoastingBatchRepo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@GrpcService
public class RoastingService extends RoastingServiceGrpc.RoastingServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(RoastingService.class);
    private final RoastingBatchRepo roastingBatchRepo;
    private final StockLossService service;

    public RoastingService(RoastingBatchRepo roastingBatchRepo, StockLossService service) {
        this.roastingBatchRepo = roastingBatchRepo;
        this.service = service;
    }

    @Override
    public void reportRoasting(RoastingRequest request, StreamObserver<RoastingResponse> responseObserver) {
        // Create a new RoastingBatch entity from the request
        RoastingBatchEntity batch = new RoastingBatchEntity();
        batch.setOriginCountry(request.getOriginCountry());
        batch.setCoffeeSort(request.getCoffeeSort());
        batch.setOutputWeight(request.getOutputWeight());
        batch.setInputWeight(request.getInputWeight());
        batch.setTeamId(UUID.fromString(request.getTeamId()));
        try {
            // Save the batch to the database
            RoastingBatchEntity savedBatch = roastingBatchRepo.save(batch);

            // Build the gRPC response
            RoastingResponse response = RoastingResponse.newBuilder()
                    .setMessage("Roasting data saved successfully")
                    .setStatus("SUCCESS")
                    .build();

            // Send the response and complete the stream
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            logger.info("Saved roasting batch data: {}", batch);

            //count loss and update
            service.updateLoss(savedBatch.getBatchId(), savedBatch.getInputWeight(), savedBatch.getOutputWeight());
            //reflect stock remainders
            service.updateStock(savedBatch);
        } catch (Exception e) {
            logger.error("Failed to save roasting batch data: {}", batch, e);
            // Build a gRPC error status and propagate it to the client
            RoastingResponse response = RoastingResponse.newBuilder()
                    .setMessage("Failed to save roasting data")
                    .setStatus("ERROR")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

}
