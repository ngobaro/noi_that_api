package com.baro.noi_that_api.module.orders.service.impl;

import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.orders.dto.response.OrderDetailResponse;
import com.baro.noi_that_api.module.orders.mapper.OrderDetailMapper;
import com.baro.noi_that_api.module.orders.repository.OrderDetailRepository;
import com.baro.noi_that_api.module.orders.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderDetailResponse> getByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(orderDetailMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailResponse getById(Integer id) {
        return orderDetailMapper.toResponse(
                orderDetailRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND))
        );
    }
}