package com.baro.noi_that_api.module.orders.dto.response;

import com.baro.noi_that_api.module.orders.entity.Order.OrderStatus;
import com.baro.noi_that_api.module.orders.entity.Order.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Integer id;
    private Integer customerId;
    private Integer promotionId;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private PaymentMethod method;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderDetailResponse> orderDetails;
}