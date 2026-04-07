package com.baro.noi_that_api.module.orders.mapper;

import com.baro.noi_that_api.module.orders.dto.request.OrderCreateRequest.OrderDetailRequest;
import com.baro.noi_that_api.module.orders.dto.response.OrderDetailResponse;
import com.baro.noi_that_api.module.orders.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderDetail toEntity(OrderDetailRequest request);

    OrderDetailResponse toResponse(OrderDetail orderDetail);
}