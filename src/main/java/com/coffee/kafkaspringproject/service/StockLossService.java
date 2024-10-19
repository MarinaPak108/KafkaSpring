package com.coffee.kafkaspringproject.service;

import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
import com.coffee.kafkaspringproject.repo.RoastingBatchRepo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class StockLossService {

    private final CoffeeBagRepo coffeeBagRepo;
    private final RoastingBatchRepo roastingBatchRepo;

    public StockLossService(CoffeeBagRepo coffeeBagRepo, RoastingBatchRepo roastingBatchRepo) {
        this.coffeeBagRepo = coffeeBagRepo;
        this.roastingBatchRepo = roastingBatchRepo;
    }

    @Transactional
    public void updateStock(RoastingBatchEntity batch){
        List<CoffeeBagEntity> bags = coffeeBagRepo.findByCountryAndSortWithWeightLeftNotZero(batch.getOriginCountry(), batch.getCoffeeSort());
        //!!considering that received messages are correct, batch.inWeight always less then sum of bagS.leftWeight
        int toSubstractWeight = batch.getInputWeight(); // 120 000  // 50 000
        int sumStock = coffeeBagRepo.sumWeightLeftByOriginCountryAndCoffeeSort(batch.getCoffeeSort(), batch.getOriginCountry());
        int bagsIndex = 0;
        while(toSubstractWeight>0 & bagsIndex<bags.size()){
            CoffeeBagEntity bag = bags.get(bagsIndex);
            int toRecordWeight=0;
            if(toSubstractWeight >= bag.getWeightLeft()){ // 120 000 vs 70 000 // 50 000 vs 300 000
                toSubstractWeight -= bag.getWeightLeft(); //50 000
            }
            else{
                toRecordWeight = bag.getWeightLeft()-toSubstractWeight; //     // 300 000 - 50 000 = 250 000
                toSubstractWeight=0;
            }
            bag.setWeightLeft(toRecordWeight);
            coffeeBagRepo.save(bag);
            bagsIndex++;
        }
    }

    @Transactional
    public void updateLoss(Long batchId, int input, int output){
        //weight loss calculation:
        double loss = (input-output)*100/input;
        //update value
        roastingBatchRepo.updateLossPercentage(batchId, loss);
    }

    public int getFilteredStock(String country, Integer coffeeSort) {
        if (country != null && coffeeSort != null) {
            return coffeeBagRepo.sumWeightLeftByOriginCountryAndCoffeeSort(coffeeSort,country);
        } else if (country  != null) {
            return coffeeBagRepo.sumWeightLeftByOriginCountry(country);
        } else if (coffeeSort != null) {
            return coffeeBagRepo.sumWeightLeftByCoffeeSort(coffeeSort);
        } else {
            return coffeeBagRepo.sumWeightLeft();
        }
    }

    public double getLossPercentage(String teamId, String country) {
        // add if no records regarding combination team id + country in db then throw msg with error
        if(teamId != null && country != null){
            return roastingBatchRepo.findAverageLossByCountryAndTeamId(country, teamId);
        } else if (country != null){
            return roastingBatchRepo.findAverageLossByCountry(country);
        } else if(teamId != null){
            return roastingBatchRepo.findAverageLossByTeamId(teamId);
        } else {
            return roastingBatchRepo.findAverageLoss();
        }
    }
}
