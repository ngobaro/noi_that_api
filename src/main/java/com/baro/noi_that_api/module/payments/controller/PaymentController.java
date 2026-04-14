package com.baro.noi_that_api.module.payments.controller;

import com.baro.noi_that_api.common.dto.ApiResponse;
import com.baro.noi_that_api.module.orders.service.OrderService; // thêm để check ownership
import com.baro.noi_that_api.module.payments.dto.request.PaymentCreateRequest;
import com.baro.noi_that_api.module.payments.dto.response.PaymentResponse;
import com.baro.noi_that_api.module.payments.dto.response.VNPayResponse;
import com.baro.noi_that_api.module.payments.service.PaymentService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internal/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService; // thêm để kiểm tra ownership

    @PostMapping
    public ApiResponse<PaymentResponse> create(
            @Valid @RequestBody PaymentCreateRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .code(201)
                .message("Tạo payment thành công")
                .result(paymentService.create(request))
                .build();
    }

    @PostMapping("/vnpay/create")
    public ApiResponse<VNPayResponse> createVNPay(
            @RequestParam Integer orderId,
            HttpServletRequest request) {
        return ApiResponse.<VNPayResponse>builder()
                .code(200)
                .result(paymentService.createVNPay(orderId, request))
                .build();
    }

    @GetMapping("/vnpay/callback")
    public ApiResponse<Map<String, Object>> handleVNPayCallback(
            @RequestParam Map<String, String> queryParams) {
        return ApiResponse.<Map<String, Object>>builder()
                .code(200)
                .result(paymentService.handleVNPayCallback(queryParams))
                .build();
    }

    @GetMapping("/vnpay/return")
    @PermitAll()
    public ResponseEntity<Void> vnpayReturn(HttpServletRequest request) {
        // giữ nguyên code cũ của bạn
        Map<String, String[]> paramMap = request.getParameterMap();
        System.out.println("====== VNPAY RETURN (User Redirect) ======");
        paramMap.forEach((key, values) ->
                System.out.println(key + " = " + String.join(", ", values)));

        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");

        String redirectUrl = "http://localhost:3000/payment-result";

        if ("00".equals(responseCode)) {
            redirectUrl += "?status=success&txnRef=" + txnRef;
        } else {
            redirectUrl += "?status=failed&txnRef=" + txnRef;
        }

        return ResponseEntity.status(302)
                .header("Location", redirectUrl)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PaymentResponse> getById(@PathVariable Integer id) {
        PaymentResponse payment = paymentService.getById(id);
        // Check ownership qua order
        var order = orderService.getById(payment.getOrderId());

        return ApiResponse.<PaymentResponse>builder()
                .code(200)
                .result(payment)
                .build();
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<PaymentResponse> getByOrderId(@PathVariable Integer orderId) {
        var order = orderService.getById(orderId);

        return ApiResponse.<PaymentResponse>builder()
                .code(200)
                .result(paymentService.getByOrderId(orderId))
                .build();
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        paymentService.updateStatus(id, status);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Cập nhật trạng thái thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<List<PaymentResponse>> getAll() {
        List<PaymentResponse> payments = paymentService.getAll();
        return ApiResponse.<List<PaymentResponse>>builder()
                .code(200)
                .result(payments)
                .build();
    }
}