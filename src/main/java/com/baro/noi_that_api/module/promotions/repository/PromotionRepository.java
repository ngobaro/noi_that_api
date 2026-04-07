package com.baro.noi_that_api.module.promotions.repository;

import com.baro.noi_that_api.module.promotions.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    Optional<Promotion> findByCode(String code);

    boolean existsByCode(String code);

    List<Promotion> findByIsActiveTrue();

    @Query("SELECT p FROM Promotion p WHERE p.isActive = true " +
            "AND p.startDay <= :today AND p.endDay >= :today")
    List<Promotion> findActiveAndValid(LocalDate today);
}