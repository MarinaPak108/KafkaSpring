package com.coffee.kafkaspringproject.repo;

import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoastingBatchRepo extends JpaRepository<RoastingBatchEntity, Long> {
    //update loss percentage by batchId
    @Query("UPDATE RoastingBatchEntity b SET b.lossPercentage = :lossPercentage WHERE b.batchId = :id")
    void updateLossPercentage(@Param("id") Long id, @Param("lossPercentage") double lossPercentage);
    //filter by teamId and get mean loss percentage value
    @Query("SELECT AVG(r.lossPercentage) FROM RoastingBatchEntity r WHERE r.teamId = :teamId AND r.lossPercentage > 0")
    Double findAverageLossByTeamId(@Param("teamId") String teamId);
    //filter by country and get mean loss percentage value
    @Query("SELECT AVG(r.lossPercentage) FROM RoastingBatchEntity r WHERE r.originCountry = :country AND r.lossPercentage > 0")
    Double findAverageLossByCountry(@Param("country") String country);
    //filter by teamId and country and get mean loss percentage value
    @Query("SELECT AVG(r.lossPercentage) FROM RoastingBatchEntity r WHERE r.originCountry = :country AND r.teamId = :teamId AND r.lossPercentage > 0")
    Double findAverageLossByCountryAndTeamId(@Param("country") String country, @Param("teamId") String teamId );
    //get mean loss percentage value
    @Query("SELECT AVG(r.lossPercentage) FROM RoastingBatchEntity r WHERE r.lossPercentage > 0")
    Double findAverageLoss();
}
