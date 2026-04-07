package com.baro.noi_that_api.module.promotions.dto.response;

import com.baro.noi_that_api.module.promotions.entity.Promotion.PromotionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionResponse {

    private Integer id;
    private String code;
    private String name;
    private String description;
    private PromotionType type;
    private BigDecimal value;
    private LocalDate startDay;
    private LocalDate endDay;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}