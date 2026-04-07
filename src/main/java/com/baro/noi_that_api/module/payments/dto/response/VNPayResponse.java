package com.baro.noi_that_api.module.payments.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VNPayResponse {

    private String paymentUrl;
    private String transactionId;
}