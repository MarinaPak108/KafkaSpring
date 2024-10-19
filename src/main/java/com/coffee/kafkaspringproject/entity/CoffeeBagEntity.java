package com.coffee.kafkaspringproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coffee_bags")
public class CoffeeBagEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Auto-increment primary key

    @Column(name = "origin_country", length = 100)
    private String originCountry;  // Страна происхождения
    @Column(name = "arabica_percentage")
    private double arabicaPercentage;  // Процент арабики
    @Column(name = "robusta_percentage")
    private double robustaPercentage;  // Процент робусты

    @Column(name = "weight_in_gram", nullable = false)
    private int weightInGr;  // Вес мешка (60 кг) => 60000

    @Column(name = "weight_left")
    private int weightLeft;

    @Column(name = "coffee_sort", nullable = false)
    private int coffeeSort;

    public CoffeeBagEntity() {
        // Default constructor for JPA
    }
    // will recieve number of bags and convert to kg
    public CoffeeBagEntity(String originCountry, double arabicaPercentage, double robustaPercentage, int nrOfBags, int coffeeSort) {
        //convert bags to kg, considering 1 bag is 60 kg => 60000 gram
        int weightInGr = nrOfBags*60000;

        this.originCountry = originCountry;
        this.arabicaPercentage = arabicaPercentage;
        this.robustaPercentage = robustaPercentage;
        this.weightInGr = weightInGr;
        //by default weight left equals to weight in kg
        this.weightLeft = weightInGr;
        this.coffeeSort = coffeeSort;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getOriginCountry() { return originCountry; }
    public void setOriginCountry(String originCountry) { this.originCountry = originCountry; }

    public double getArabicaPercentage() { return arabicaPercentage; }
    public void setArabicaPercentage(double arabicaPercentage) { this.arabicaPercentage = arabicaPercentage; }

    public double getRobustaPercentage() { return robustaPercentage; }
    public void setRobustaPercentage(double robustaPercentage) { this.robustaPercentage = robustaPercentage; }

    public int getWeightLeft() {
        return weightLeft;
    }

    public void setWeightLeft(int weightLeft) {
        this.weightLeft = weightLeft;
    }

    public int getWeightInGr() {
        return weightInGr;
    }

    public void setWeightInGr(int weightInGr) {
        this.weightInGr = weightInGr;
    }

    public int getCoffeeSort() {
        return coffeeSort;
    }

    public void setCoffeeSort(int coffeeSort) {
        this.coffeeSort = coffeeSort;
    }

    @Override
    public String toString() {
        return "CoffeeBagEntity{" +
                "id=" + id +
                ", originCountry='" + originCountry + '\'' +
                ", arabicaPercentage=" + arabicaPercentage +
                ", robustaPercentage=" + robustaPercentage +
                ", weightInGr=" + weightInGr +
                ", weightLeft=" + weightLeft +
                ", coffeeSort=" + coffeeSort +
                '}';
    }
}
