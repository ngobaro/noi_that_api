package com.baro.noi_that_api.module.orders.controller;

import com.baro.noi_that_api.common.dto.ApiResponse;
import com.baro.noi_that_api.common.security.SecurityUtils;
import com.baro.noi_that_api.module.orders.dto.request.OrderCreateRequest;
import com.baro.noi_that_api.module.orders.dto.request.OrderStatusUpdateRequest;
import com.baro.noi_that_api.module.orders.dto.response.OrderDetailResponse;
import com.baro.noi_that_api.module.orders.dto.response.OrderResponse;
import com.baro.noi_that_api.module.orders.service.OrderDetailService;
import com.baro.noi_that_api.module.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @PostMapping("/orders")
    public ApiResponse<OrderResponse> create(
            @Valid @RequestBody OrderCreateRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .code(201)
                .message("Tạo đơn hàng thành công")
                .result(orderService.create(request))
                .build();
    }

    @GetMapping("/orders/{id}")
    public ApiResponse<OrderResponse> getById(@PathVariable Integer id) {
        OrderResponse order = orderService.getById(id);
        if (SecurityUtils.isCustomer() && !order.getCustomerId().equals(SecurityUtils.getCurrentId())) {
            throw new AccessDeniedException("Chỉ được xem đơn hàng của chính mình");
        }
        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .result(order)
                .build();
    }

    @GetMapping("/orders/customer/{customerId}")
    public ApiResponse<List<OrderResponse>> getByCustomerId(
            @PathVariable Integer customerId) {
        if (SecurityUtils.isCustomer() && !customerId.equals(SecurityUtils.getCurrentId())) {
            throw new AccessDeniedException("Chỉ được xem đơn hàng của chính mình");
        }
        return ApiResponse.<List<OrderResponse>>builder()
                .code(200)
                .result(orderService.getByCustomerId(customerId))
                .build();
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderResponse>> getAll() {
        if (SecurityUtils.isCustomer()) {
            throw new AccessDeniedException("Customer không được xem danh sách tất cả đơn hàng");
        }
        return ApiResponse.<List<OrderResponse>>builder()
                .code(200)
                .result(orderService.getAll())
                .build();
    }

    @PutMapping("/orders/{id}/status")
    public ApiResponse<OrderResponse> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStaff()) {
            throw new AccessDeniedException("Chỉ ADMIN/STAFF mới cập nhật trạng thái đơn hàng");
        }
        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .result(orderService.updateStatus(id, request))
                .build();
    }

    @PutMapping("/orders/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Integer id) {
        OrderResponse order = orderService.getById(id);
        if (SecurityUtils.isCustomer() && !order.getCustomerId().equals(SecurityUtils.getCurrentId())) {
            throw new AccessDeniedException("Chỉ được hủy đơn hàng của chính mình");
        }
        orderService.cancel(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Hủy đơn hàng thành công")
                .build();
    }

    // ===================== ORDER DETAIL =====================

    @GetMapping("/order-details/order/{orderId}")
    public ApiResponse<List<OrderDetailResponse>> getDetailsByOrderId(
            @PathVariable Integer orderId) {
        OrderResponse order = orderService.getById(orderId);
        if (SecurityUtils.isCustomer() && !order.getCustomerId().equals(SecurityUtils.getCurrentId())) {
            throw new AccessDeniedException("Chỉ được xem chi tiết đơn hàng của chính mình");
        }
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .code(200)
                .result(orderDetailService.getByOrderId(orderId))
                .build();
    }

    @GetMapping("/order-details/{id}")
    public ApiResponse<OrderDetailResponse> getDetailById(
            @PathVariable Integer id) {
        // Nếu cần check ownership chi tiết hơn thì bổ sung sau
        return ApiResponse.<OrderDetailResponse>builder()
                .code(200)
                .result(orderDetailService.getById(id))
                .build();
    }
}