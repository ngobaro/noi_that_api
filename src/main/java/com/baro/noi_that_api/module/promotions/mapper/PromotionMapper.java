package com.baro.noi_that_api.module.promotions.mapper;

import com.baro.noi_that_api.module.promotions.dto.request.PromotionCreateRequest;
import com.baro.noi_that_api.module.promotions.dto.request.PromotionUpdateRequest;
import com.baro.noi_that_api.module.promotions.dto.response.PromotionResponse;
import com.baro.noi_that_api.module.promotions.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Promotion toEntity(PromotionCreateRequest request);

    PromotionResponse toResponse(Promotion promotion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(PromotionUpdateRequest request, @MappingTarget Promotion promotion);
}