package com.baro.noi_that_api.module.payments.service.impl;

import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.payments.dto.request.PaymentCreateRequest;
import com.baro.noi_that_api.module.payments.dto.response.PaymentResponse;
import com.baro.noi_that_api.module.payments.dto.response.VNPayResponse;
import com.baro.noi_that_api.module.payments.entity.Payment;
import com.baro.noi_that_api.module.payments.mapper.PaymentMapper;
import com.baro.noi_that_api.module.payments.repository.PaymentRepository;
import com.baro.noi_that_api.module.payments.service.PaymentService;
import com.baro.noi_that_api.module.payments.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Value("${vnpay.tmnCode}")
    private String tmnCode;

    @Value("${vnpay.secretKey}")
    private String secretKey;

    @Value("${vnpay.payUrl}")
    private String payUrl;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    @Override
    public PaymentResponse create(PaymentCreateRequest request) {
        if(paymentRepository.existsByOrderId(request.getOrderId())){
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .method(request.getMethod())
                .amount(request.getAmount())
                .transactionId("")
                .status(Payment.PaymentStatus.pending)
                .build();

        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Override
    public VNPayResponse createVNPay(Integer orderId, HttpServletRequest request) {
        // Lấy payment từ DB
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        // Tạo txnRef duy nhất
        String txnRef = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 32);

        // Lưu txnRef vào payment
        payment.setTransactionId(txnRef);
        paymentRepository.save(payment);

        // Build params VNPay — dùng TreeMap để tự sort theo key
        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(
                payment.getAmount().multiply(BigDecimal.valueOf(100)).longValue()));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang - " + orderId);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_IpAddr", getSingleIp(request));

        // ======= FIX múi giờ VN =======
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");

        // vnp_CreateDate theo giờ VN
        LocalDateTime nowVN = LocalDateTime.now(vnZone);
        vnpParams.put("vnp_CreateDate", nowVN.format(formatter));

        // vnp_ExpireDate +15 phút theo giờ VN
        LocalDateTime expireVN = nowVN.plusMinutes(15);
        vnpParams.put("vnp_ExpireDate", expireVN.format(formatter));
        // ================================

        // Tạo chuỗi ký
        String signData = vnpParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + urlEncode(e.getValue()))
                .collect(Collectors.joining("&"));

        String secureHash = VNPayUtil.hmacSHA512(secretKey, signData);

        String paymentUrl = payUrl + "?" + signData + "&vnp_SecureHash=" + secureHash;

        System.out.println("====== CREATE VNPAY PAYMENT ======");
        System.out.println("OrderId: " + orderId);
        System.out.println("TxnRef: " + txnRef);
        System.out.println("Payment URL: " + paymentUrl);

        return VNPayResponse.builder()
                .paymentUrl(paymentUrl)
                .transactionId(txnRef)
                .build();
    }

    @Override
    public Map<String, Object> handleVNPayCallback(Map<String, String> queryParams) {
        System.out.println("====== VNPAY CALLBACK ======");
        System.out.println("Params: " + queryParams);

        Map<String, Object> result = new HashMap<>();

        String txnRef = queryParams.get("vnp_TxnRef");
        String responseCode = queryParams.get("vnp_ResponseCode");
        String receivedHash = queryParams.get("vnp_SecureHash");

        // Tìm payment theo transactionId
        Payment payment = paymentRepository.findByTransactionId(txnRef)
                .orElse(null);

        if (payment == null) {
            System.out.println("❌ Không tìm thấy txnRef: " + txnRef);
            result.put("status", "ERROR");
            return result;
        }

        // Verify chữ ký
        Map<String, String> paramsToVerify = new TreeMap<>(queryParams);
        paramsToVerify.remove("vnp_SecureHash");
        paramsToVerify.remove("vnp_SecureHashType");

        String verifyData = paramsToVerify.entrySet().stream()
                .map(e -> e.getKey() + "=" + urlEncode(e.getValue()))
                .collect(Collectors.joining("&"));

        String calculatedHash = VNPayUtil.hmacSHA512(secretKey, verifyData);

        if (!calculatedHash.equals(receivedHash)) {
            System.out.println("❌ Sai chữ ký");
            payment.setStatus(Payment.PaymentStatus.failed);
            paymentRepository.save(payment);
            result.put("status", "FAILED");
            return result;
        }

        // Xử lý kết quả
        if ("00".equals(responseCode)) {
            System.out.println("✅ Thanh toán SUCCESS");
            payment.setStatus(Payment.PaymentStatus.success);
            result.put("status", "SUCCESS");
        } else {
            System.out.println("❌ Thanh toán FAILED: " + responseCode);
            payment.setStatus(Payment.PaymentStatus.failed);
            result.put("status", "FAILED");
        }

        paymentRepository.save(payment);
        result.put("txnRef", txnRef);
        result.put("orderId", payment.getOrderId());

        return result;
    }

    @Override
    public PaymentResponse getById(Integer id) {
        return paymentMapper.toResponse(
                paymentRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND)));
    }

    @Override
    public PaymentResponse getByOrderId(Integer orderId) {
        return paymentMapper.toResponse(
                paymentRepository.findByOrderId(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND)));
    }

    @Override
    public void updateStatus(Integer id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        payment.setStatus(Payment.PaymentStatus.valueOf(status.toLowerCase()));
        paymentRepository.save(payment);
    }

    // ==================== PRIVATE ====================

    private String getSingleIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0].trim();
        }
        ip = request.getRemoteAddr();
        return ip != null ? ip : "127.0.0.1";
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return value;
        }
    }

    @Override
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }
}