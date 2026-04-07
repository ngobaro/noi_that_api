package com.baro.noi_that_api.module.payments.dto.request;

import com.baro.noi_that_api.module.payments.entity.Payment.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentCreateRequest {

    @NotNull(message = "Order ID không được để trống")
    private Integer orderId;

    @NotNull(message = "Phương thức không được để trống")
    private PaymentMethod method;

    @NotNull(message = "Số tiền không được để trống")
    private BigDecimal amount;
}