package com.coffee.kafkaspringproject.config;

import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class CoffeeBagSerializer implements Serializer<CoffeeBagEntity> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No special configuration needed
    }

    @Override
    public byte[] serialize(String topic, CoffeeBagEntity data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing CoffeeBag", e);
        }
    }

    @Override
    public void close() {
        // No resources to close
    }
}

