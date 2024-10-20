package com.coffee.kafkaspringproject.service;

import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import com.coffee.kafkaspringproject.repo.CoffeeBagRepo;
import com.coffee.kafkaspringproject.repo.RoastingBatchRepo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Integer sumStock = coffeeBagRepo.sumWeightLeftByOriginCountryAndCoffeeSort(batch.getCoffeeSort(), batch.getOriginCountry());
        int bagsIndex = 0;
        while(toSubstractWeight>0 & bagsIndex<bags.size()){
            CoffeeBagEntity bag = bags.get(bagsIndex);
            int toRecordWeight=0;
            if(toSubstractWeight >= bag.getWeightLeft()){
                toSubstractWeight -= bag.getWeightLeft();
            }
            else{
                toRecordWeight = bag.getWeightLeft()-toSubstractWeight;
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
        Double averageLoss = null;
        // add if no records regarding combination team id + country in db then throw msg with error
        if(teamId != null && country != null){
            averageLoss = roastingBatchRepo.findAverageLossByCountryAndTeamId(country, teamId);
            checkIfAvaliabble(averageLoss, "No records found for the combination of teamId: " + teamId + " and country: " + country);
            return averageLoss;
        } else if (country != null){
            averageLoss= roastingBatchRepo.findAverageLossByCountry(country);
            checkIfAvaliabble(averageLoss,"No records found for country: " + country);
            return averageLoss;
        } else if(teamId != null){
            averageLoss= roastingBatchRepo.findAverageLossByTeamId(teamId);
            checkIfAvaliabble(averageLoss,"No records found for teamId: " + teamId);
            return averageLoss;
        } else {
            return roastingBatchRepo.findAverageLoss();
        }
    }

    private void checkIfAvaliabble(Double value, String msg){
        if (value == null) {
            throw new IllegalArgumentException(msg);
        }
    }
}
