package com.baro.noi_that_api.module.promotions.mapper;

import com.baro.noi_that_api.module.promotions.dto.request.CategoryPromotionRequest;
import com.baro.noi_that_api.module.promotions.dto.response.CategoryPromotionResponse;
import com.baro.noi_that_api.module.promotions.entity.CategoryPromotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryPromotionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CategoryPromotion toEntity(CategoryPromotionRequest request);

    CategoryPromotionResponse toResponse(CategoryPromotion categoryPromotion);
}