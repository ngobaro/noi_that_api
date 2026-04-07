package com.baro.noi_that_api.module.promotions.service;

import com.baro.noi_that_api.module.promotions.dto.request.PromotionCreateRequest;
import com.baro.noi_that_api.module.promotions.dto.request.PromotionUpdateRequest;
import com.baro.noi_that_api.module.promotions.dto.response.PromotionResponse;

import java.util.List;

public interface PromotionService {

    // Internal - Node.js gọi
    PromotionResponse getById(Integer id);
    PromotionResponse getByCode(String code);
    PromotionResponse validateCode(String code);        // validate + kiểm tra còn hạn
    List<PromotionResponse> getActive();

    // Admin
    List<PromotionResponse> getAll();
    PromotionResponse create(PromotionCreateRequest request);
    PromotionResponse update(Integer id, PromotionUpdateRequest request);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
}