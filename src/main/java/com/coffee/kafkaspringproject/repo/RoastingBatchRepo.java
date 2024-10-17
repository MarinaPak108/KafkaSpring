package com.coffee.kafkaspringproject.repo;

import com.coffee.kafkaspringproject.entity.RoastingBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoastingBatchRepo extends JpaRepository<RoastingBatchEntity, Long> {
}
