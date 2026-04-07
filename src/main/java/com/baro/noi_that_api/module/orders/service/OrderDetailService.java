package com.baro.noi_that_api.module.orders.service;

import com.baro.noi_that_api.module.orders.dto.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetailResponse> getByOrderId(Integer orderId);
    OrderDetailResponse getById(Integer id);
}