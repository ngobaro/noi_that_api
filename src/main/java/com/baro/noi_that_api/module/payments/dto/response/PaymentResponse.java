package com.baro.noi_that_api.module.payments.dto.response;

import com.baro.noi_that_api.module.payments.entity.Payment.PaymentMethod;
import com.baro.noi_that_api.module.payments.entity.Payment.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Integer id;
    private Integer orderId;
    private PaymentMethod method;
    private String transactionId;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}