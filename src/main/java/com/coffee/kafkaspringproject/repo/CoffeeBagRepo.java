package com.coffee.kafkaspringproject.repo;

import com.coffee.kafkaspringproject.entity.CoffeeBagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeBagRepo extends JpaRepository<CoffeeBagEntity, Long> {}

