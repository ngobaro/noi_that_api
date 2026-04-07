package com.baro.noi_that_api.module.promotions.repository;

import com.baro.noi_that_api.module.promotions.entity.CategoryPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryPromotionRepository extends JpaRepository<CategoryPromotion, Integer> {

    List<CategoryPromotion> findByCategoryId(Integer categoryId);

    List<CategoryPromotion> findByPromotionId(Integer promotionId);

    boolean existsByCategoryIdAndPromotionId(Integer categoryId, Integer promotionId);
}