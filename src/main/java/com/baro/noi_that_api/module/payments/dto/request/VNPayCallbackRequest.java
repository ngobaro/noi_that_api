package com.baro.noi_that_api.module.payments.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class VNPayCallbackRequest {
    private Map<String, String> queryParams;
}