package com.baro.noi_that_api.module.payments.service;

import com.baro.noi_that_api.module.payments.dto.request.PaymentCreateRequest;
import com.baro.noi_that_api.module.payments.dto.response.PaymentResponse;
import com.baro.noi_that_api.module.payments.dto.response.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    // Tạo payment record (COD)
    PaymentResponse create(PaymentCreateRequest request);

    // Tạo VNPay URL
    VNPayResponse createVNPay(Integer orderId, HttpServletRequest request);

    // Xử lý VNPay callback
    Map<String, Object> handleVNPayCallback(Map<String, String> queryParams);

    // Query
    PaymentResponse getById(Integer id);
    PaymentResponse getByOrderId(Integer orderId);

    // Cập nhật status
    void updateStatus(Integer id, String status);

    List<PaymentResponse> getAll();
}