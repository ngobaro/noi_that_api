package com.baro.noi_that_api.module.payments.mapper;

import com.baro.noi_that_api.module.payments.dto.response.PaymentResponse;
import com.baro.noi_that_api.module.payments.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponse toResponse(Payment payment);
}