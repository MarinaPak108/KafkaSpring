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
    @Column(name = "weight_in_grams", nullable = false)
    private int weightInGrams = 60000;  // Вес мешка (60 кг в граммах)

    public CoffeeBagEntity() {
        // Default constructor for JPA
    }

    public CoffeeBagEntity(String originCountry, double arabicaPercentage, double robustaPercentage, int weightInGrams) {
        this.originCountry = originCountry;
        this.arabicaPercentage = arabicaPercentage;
        this.robustaPercentage = robustaPercentage;
        this.weightInGrams = weightInGrams;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getOriginCountry() { return originCountry; }
    public void setOriginCountry(String originCountry) { this.originCountry = originCountry; }

    public double getArabicaPercentage() { return arabicaPercentage; }
    public void setArabicaPercentage(double arabicaPercentage) { this.arabicaPercentage = arabicaPercentage; }

    public double getRobustaPercentage() { return robustaPercentage; }
    public void setRobustaPercentage(double robustaPercentage) { this.robustaPercentage = robustaPercentage; }

    public int getWeightInGrams() { return weightInGrams; }
    public void setWeightInGrams(int weightInGrams) { this.weightInGrams = weightInGrams; }

    @Override
    public String toString() {
        return "CoffeeBag{" +
                "id=" + id +
                ", originCountry='" + originCountry + '\'' +
                ", arabicaPercentage=" + arabicaPercentage +
                ", robustaPercentage=" + robustaPercentage +
                ", weightInGrams=" + weightInGrams +
                '}';
    }
}
