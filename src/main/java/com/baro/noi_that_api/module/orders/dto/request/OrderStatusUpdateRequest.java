package com.baro.noi_that_api.module.orders.dto.request;

import com.baro.noi_that_api.module.orders.entity.Order.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private OrderStatus status;

    private String note;
}