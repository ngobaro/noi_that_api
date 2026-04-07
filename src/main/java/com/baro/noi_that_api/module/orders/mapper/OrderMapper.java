package com.baro.noi_that_api.module.orders.mapper;

import com.baro.noi_that_api.module.orders.dto.request.OrderCreateRequest;
import com.baro.noi_that_api.module.orders.dto.response.OrderResponse;
import com.baro.noi_that_api.module.orders.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    Order toEntity(OrderCreateRequest request);

    OrderResponse toResponse(Order order);
}