package com.baro.noi_that_api.module.promotions.dto.request;

import com.baro.noi_that_api.module.promotions.entity.Promotion.PromotionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PromotionCreateRequest {

    @NotBlank(message = "Mã không được để trống")
    private String code;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Loại không được để trống")
    private PromotionType type;

    @NotNull(message = "Giá trị không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị phải lớn hơn 0")
    private BigDecimal value;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDay;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDay;
}