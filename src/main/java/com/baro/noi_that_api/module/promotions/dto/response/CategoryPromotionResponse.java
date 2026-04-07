package com.baro.noi_that_api.module.promotions.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryPromotionResponse {

    private Integer id;
    private Integer categoryId;
    private Integer promotionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}