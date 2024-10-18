package com.coffee.kafkaspringproject.repo;

import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoastingBatchRepo extends JpaRepository<RoastingBatchEntity, Long> {
    //find all batches with zero loss percentage
    List<RoastingBatchEntity> findByLossPercentage(double lossPercentage);

    //find batch by id
    RoastingBatchEntity findRoastingBatchEntityByBatchId(Long batchId);

    @Modifying
    @Query("UPDATE RoastingBatchEntity b SET b.lossPercentage = :lossPercentage WHERE b.batchId = :id")
    void updateLossPercentage(@Param("id") Long id, @Param("lossPercentage") double lossPercentage);

    @Query("SELECT AVG(r.lossPercentage) FROM RoastingBatchEntity r WHERE r.teamId = :teamId AND r.lossPercentage > 0")
    Double findAverageLossByTeamId(@Param("teamId") String teamId);

    @Query("SELECT AVG(r.lossPercentage) FROM RoastingBatchEntity r WHERE r.originCountry = :country AND r.lossPercentage > 0")
    Double findAverageLossByCountry(@Param("country") String country);

    @Query("SELECT AVG(r.lossPercentage) FROM RoastingBatchEntity r WHERE r.originCountry = :country AND r.teamId = :teamId AND r.lossPercentage > 0")
    Double findAverageLossByCountryAndTeamId(@Param("country") String country, @Param("teamId") String teamId );

    @Query("SELECT AVG(r.lossPercentage) FROM RoastingBatchEntity r WHERE r.lossPercentage > 0")
    Double findAverageLoss();
}
