package com.baro.noi_that_api.module.orders.service;

import com.baro.noi_that_api.module.orders.dto.request.OrderCreateRequest;
import com.baro.noi_that_api.module.orders.dto.request.OrderStatusUpdateRequest;
import com.baro.noi_that_api.module.orders.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse create(OrderCreateRequest request);
    OrderResponse getById(Integer id);
    List<OrderResponse> getByCustomerId(Integer customerId);
    List<OrderResponse> getAll();
    OrderResponse updateStatus(Integer id, OrderStatusUpdateRequest request);
    void cancel(Integer id);
}