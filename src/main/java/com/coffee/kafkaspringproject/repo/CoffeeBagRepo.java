package com.coffee.kafkaspringproject.repo;

import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoffeeBagRepo extends JpaRepository<CoffeeBagEntity, Long> {
    // Query to retrieve list of all filtered records
    @Query("SELECT r FROM CoffeeBagEntity r WHERE r.weightLeft > 0 AND r.originCountry = :originCountry AND r.coffeeSort = :coffeeSort")
    List<CoffeeBagEntity> findByCountryAndSortWithWeightLeftNotZero(
            @Param("originCountry") String originCountry,
            @Param("coffeeSort") int coffeeSort);

    // Query to retrieve all filtered records by country and coffee sort and sum the 'outputWeight' column
    @Query("SELECT SUM(r.weightLeft) FROM CoffeeBagEntity r WHERE r.coffeeSort = :coffeeSort AND r.originCountry = :originCountry AND r.weightLeft > 0")
    Integer sumWeightLeftByOriginCountryAndCoffeeSort(
            @Param("coffeeSort") int coffeeSort,
            @Param("originCountry") String originCountry);

    // Query to retrieve all filtered records by country and sum the 'outputWeight' column
    @Query("SELECT SUM(r.weightLeft) FROM CoffeeBagEntity r WHERE r.originCountry = :originCountry AND r.weightLeft > 0")
    Integer sumWeightLeftByOriginCountry(
            @Param("originCountry") String originCountry);

    // Query to retrieve all filtered records by sort and sum the 'outputWeight' column
    @Query("SELECT SUM(r.weightLeft) FROM CoffeeBagEntity r WHERE r.coffeeSort = :coffeeSort AND r.weightLeft > 0")
    Integer sumWeightLeftByCoffeeSort(
            @Param("coffeeSort") int coffeeSort);

    // Query to retrieve all filtered records and sum the 'outputWeight' column
    @Query("SELECT SUM(r.weightLeft) FROM CoffeeBagEntity r WHERE r.weightLeft > 0")
    Integer sumWeightLeft();

    // find coffeeBagEntity by incoming values in db
    Optional<CoffeeBagEntity> findCoffeeBagEntityByOriginCountryAndArabicaPercentageAndRobustaPercentageAndWeightLeftAndCoffeeSort
            (String originCountry,
             double arabica,
             double robusta,
             int weightLeft,
             int coffeeSort);

}

