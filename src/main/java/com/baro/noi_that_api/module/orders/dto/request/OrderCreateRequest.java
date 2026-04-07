package com.baro.noi_that_api.module.orders.dto.request;

import com.baro.noi_that_api.module.orders.entity.Order.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {

    @NotNull(message = "Customer ID không được để trống")
    private Integer customerId;

    private Integer promotionId;

    @NotBlank(message = "Tên khách hàng không được để trống")
    private String customerName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String customerPhone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String customerAddress;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentMethod method;

    @NotNull(message = "Subtotal không được để trống")
    private BigDecimal subtotal;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotNull(message = "Tổng tiền không được để trống")
    private BigDecimal totalPrice;

    private String note;

    @NotEmpty(message = "Đơn hàng phải có ít nhất 1 sản phẩm")
    @Valid
    private List<OrderDetailRequest> items;

    @Getter
    @Setter
    public static class OrderDetailRequest {

        @NotNull(message = "Product attribute ID không được để trống")
        private Integer productAttributeId;

        @NotBlank(message = "Tên sản phẩm không được để trống")
        private String productName;

        private String productImage;

        @NotNull(message = "Số lượng không được để trống")
        private Integer quantity;

        @NotNull(message = "Đơn giá không được để trống")
        private BigDecimal unitPrice;

        @NotNull(message = "Tổng tiền không được để trống")
        private BigDecimal total;
    }
}