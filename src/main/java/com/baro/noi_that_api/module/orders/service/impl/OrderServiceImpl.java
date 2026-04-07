package com.baro.noi_that_api.module.orders.service.impl;

import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.orders.dto.request.OrderCreateRequest;
import com.baro.noi_that_api.module.orders.dto.request.OrderStatusUpdateRequest;
import com.baro.noi_that_api.module.orders.dto.response.OrderDetailResponse;
import com.baro.noi_that_api.module.orders.dto.response.OrderResponse;
import com.baro.noi_that_api.module.orders.entity.Order;
import com.baro.noi_that_api.module.orders.entity.OrderDetail;
import com.baro.noi_that_api.module.orders.mapper.OrderDetailMapper;
import com.baro.noi_that_api.module.orders.mapper.OrderMapper;
import com.baro.noi_that_api.module.orders.repository.OrderDetailRepository;
import com.baro.noi_that_api.module.orders.repository.OrderRepository;
import com.baro.noi_that_api.module.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        // Tạo order
        Order order = orderMapper.toEntity(request);
        order.setStatus(Order.OrderStatus.pending);
        Order savedOrder = orderRepository.save(order);

        // Tạo order details
        List<OrderDetail> details = request.getItems().stream()
                .map(item -> {
                    OrderDetail detail = orderDetailMapper.toEntity(item);
                    detail.setOrderId(savedOrder.getId());
                    return detail;
                })
                .collect(Collectors.toList());

        orderDetailRepository.saveAll(details);

        // Trả về response kèm details
        OrderResponse response = orderMapper.toResponse(savedOrder);
        List<OrderDetailResponse> detailResponses = details.stream()
                .map(orderDetailMapper::toResponse)
                .collect(Collectors.toList());
        response.setOrderDetails(detailResponses);

        return response;
    }

    @Override
    public OrderResponse getById(Integer id) {
        Order order = findById(id);
        OrderResponse response = orderMapper.toResponse(order);

        List<OrderDetailResponse> details = orderDetailRepository.findByOrderId(id)
                .stream()
                .map(orderDetailMapper::toResponse)
                .collect(Collectors.toList());
        response.setOrderDetails(details);

        return response;
    }

    @Override
    public List<OrderResponse> getByCustomerId(Integer customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateStatus(Integer id, OrderStatusUpdateRequest request) {
        Order order = findById(id);
        order.setStatus(request.getStatus());
        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public void cancel(Integer id) {
        Order order = findById(id);

        if (order.getStatus() == Order.OrderStatus.completed
                || order.getStatus() == Order.OrderStatus.shipping) {
            throw new AppException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.setStatus(Order.OrderStatus.cancelled);
        orderRepository.save(order);
    }

    // ==================== PRIVATE ====================

    private Order findById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }
}