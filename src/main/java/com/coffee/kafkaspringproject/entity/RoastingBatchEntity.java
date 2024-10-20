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
    @Column(name = "input_weight", nullable = false)
    private int inputWeight;
    @Column(name = "team_id", nullable = false, length = 36)
    private String teamId;
    @Column(name = "loss_percentage")
    private double lossPercentage;

    public RoastingBatchEntity() {
    }

    public RoastingBatchEntity(String originCountry, int coffeeSort, int outputWeight, int inputWeight, UUID teamId) {
        this.originCountry = originCountry;
        this.coffeeSort = coffeeSort;
        this.outputWeight = outputWeight;
        this.inputWeight = inputWeight;

        //convert to String!!!
        this.teamId = teamId.toString();
    }

    public RoastingBatchEntity(String originCountry, int coffeeSort, int outputWeight, int inputWeight, UUID teamId, int lossPercentage) {
        this.originCountry = originCountry;
        this.coffeeSort = coffeeSort;
        this.outputWeight = outputWeight;
        this.inputWeight = inputWeight;

        //convert to String!!!
        this.teamId = teamId.toString();
        this.lossPercentage = lossPercentage;
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


    public UUID getUUIDTeamId() {
        return UUID.fromString(teamId);
    }

    public  String getStringTeamId(){
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId.toString();
    }

    public int getInputWeight() {
        return inputWeight;
    }

    public void setInputWeight(int inputWeight) {
        this.inputWeight = inputWeight;
    }

    public double getLossPercentage() {
        return lossPercentage;
    }

    public void setLossPercentage(double lossPercentage) {
        this.lossPercentage = lossPercentage;
    }

    @Override
    public String toString() {
        return "RoastingBatchEntity{" +
                "batchId=" + batchId +
                ", originCountry='" + originCountry + '\'' +
                ", coffeeSort=" + coffeeSort +
                ", outputWeight=" + outputWeight +
                ", inputWeight=" + inputWeight +
                ", teamId='" + teamId + '\'' +
                ", lossPercentage=" + lossPercentage +
                '}';
    }
}
