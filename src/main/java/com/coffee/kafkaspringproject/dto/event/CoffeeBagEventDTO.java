package com.coffee.kafkaspringproject.dto.event;

public class CoffeeBagEventDTO {
    private int weightInGr;
    private String originCountry;
    private double arabicaPercentage;
    private double robustaPercentage;
    private int nrOfBags;
    private int coffeeSort;
    private int weightLeft;

    public CoffeeBagEventDTO() {
    }

    public CoffeeBagEventDTO(String originCountry, double arabicaPercentage, double robustaPercentage, int nrOfBags, int coffeeSort) {
        this.originCountry = originCountry;
        this.arabicaPercentage = arabicaPercentage;
        this.robustaPercentage = robustaPercentage;
        this.nrOfBags = nrOfBags;
        this.coffeeSort = coffeeSort;
        this.weightLeft = nrOfBags * 60000;
        this.weightInGr = nrOfBags * 60000;
    }

    // Getters and Setters
    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public double getArabicaPercentage() {
        return arabicaPercentage;
    }

    public void setArabicaPercentage(double arabicaPercentage) {
        this.arabicaPercentage = arabicaPercentage;
    }

    public double getRobustaPercentage() {
        return robustaPercentage;
    }

    public void setRobustaPercentage(double robustaPercentage) {
        this.robustaPercentage = robustaPercentage;
    }

    public int getNrOfBags() {
        return nrOfBags;
    }

    public void setNrOfBags(int nrOfBags) {
        this.nrOfBags = nrOfBags;
    }

    public int getCoffeeSort() {
        return coffeeSort;
    }

    public void setCoffeeSort(int coffeeSort) {
        this.coffeeSort = coffeeSort;
    }

    public int getWeightLeft() {
        return weightLeft;
    }

    public void setWeightLeft(int weightLeft) {
        this.weightLeft = weightLeft;
    }

    public int getWeightInGr() {
        return weightInGr;
    }
}
