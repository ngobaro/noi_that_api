package com.baro.noi_that_api.module.promotions.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPromotionRequest {

    @NotNull(message = "Category ID không được để trống")
    private Integer categoryId;

    @NotNull(message = "Promotion ID không được để trống")
    private Integer promotionId;
}