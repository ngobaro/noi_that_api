package com.baro.noi_that_api.module.promotions.service;

import com.baro.noi_that_api.module.promotions.dto.request.CategoryPromotionRequest;
import com.baro.noi_that_api.module.promotions.dto.response.CategoryPromotionResponse;

import java.util.List;

public interface CategoryPromotionService {

    List<CategoryPromotionResponse> getByCategoryId(Integer categoryId);
    List<CategoryPromotionResponse> getByPromotionId(Integer promotionId);
    CategoryPromotionResponse create(CategoryPromotionRequest request);
    void delete(Integer id);
    List<CategoryPromotionResponse> getAll();
}