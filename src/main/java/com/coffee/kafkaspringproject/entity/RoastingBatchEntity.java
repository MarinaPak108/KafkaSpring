package com.coffee.kafkaspringproject.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "roasting_batch")
public class RoastingBatchEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;
    @Column(name = "origin_country", length = 100)
    private String originCountry;
    @Column(name = "coffee_sort", nullable = false)
    private int coffeeSort;
    @Column(name = "output_weight", nullable = false)
    private int outputWeight;
    @Column(name = "team_id", nullable = false, length = 36)
    private UUID teamId;

    public RoastingBatchEntity() {
    }

    public RoastingBatchEntity(String originCountry, int coffeeSort, int outputWeight, UUID teamId) {
        this.originCountry = originCountry;
        this.coffeeSort = coffeeSort;
        this.outputWeight = outputWeight;
        this.teamId = teamId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public int getCoffeeSort() {
        return coffeeSort;
    }

    public void setCoffeeSort(int coffeeSort) {
        this.coffeeSort = coffeeSort;
    }

    public int getOutputWeight() {
        return outputWeight;
    }

    public void setOutputWeight(int outputWeight) {
        this.outputWeight = outputWeight;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "RoastingBatchEntity{" +
                "batchId=" + batchId +
                ", originCountry='" + originCountry + '\'' +
                ", coffeeSort=" + coffeeSort +
                ", outputWeight=" + outputWeight +
                ", teamId=" + teamId +
                '}';
    }
}
