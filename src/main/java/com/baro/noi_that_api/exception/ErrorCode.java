package com.baro.noi_that_api.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {

    // ===================== COMMON =====================
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST(1000, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1001, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1002, "Chưa xác thực", HttpStatus.UNAUTHORIZED),

    // ===================== USER CHUNG =====================
    USER_NOT_FOUND(2000, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    USER_INACTIVE(2001, "Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN),
    INVALID_PASSWORD(2002, "Mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(2003, "Email đã tồn tại", HttpStatus.CONFLICT),
    PASSWORD_NOT_SET(2004, "Tài khoản này chưa có mật khẩu, vui lòng đăng nhập bằng Google", HttpStatus.BAD_REQUEST),


    // ===================== OTP =====================
    OTP_INVALID(3000, "OTP không hợp lệ hoặc đã hết hạn", HttpStatus.BAD_REQUEST),
    OTP_ALREADY_SENT(3001, "OTP đã được gửi, vui lòng chờ hết hạn trước khi gửi lại", HttpStatus.TOO_MANY_REQUESTS),

    // ===================== ORDER =====================
    ORDER_NOT_FOUND(4000, "Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_NOT_FOUND(4001, "Không tìm thấy chi tiết đơn hàng", HttpStatus.NOT_FOUND),
    ORDER_CANNOT_CANCEL(4002, "Không thể hủy đơn hàng ở trạng thái này", HttpStatus.BAD_REQUEST),

    // ===================== PAYMENT =====================
    PAYMENT_NOT_FOUND(5000, "Không tìm thấy thông tin thanh toán", HttpStatus.NOT_FOUND),
    PAYMENT_INVALID_SIGNATURE(5001, "Chữ ký thanh toán không hợp lệ", HttpStatus.BAD_REQUEST),
    PAYMENT_FAILED(5002, "Thanh toán thất bại", HttpStatus.BAD_REQUEST),

    // ===================== PROMOTION =====================
    PROMOTION_NOT_FOUND(6000, "Không tìm thấy khuyến mãi", HttpStatus.NOT_FOUND),
    PROMOTION_INACTIVE(6001, "Khuyến mãi đã bị vô hiệu hóa", HttpStatus.BAD_REQUEST),
    PROMOTION_EXPIRED(6002, "Khuyến mãi đã hết hạn", HttpStatus.BAD_REQUEST),
    PROMOTION_CODE_EXISTED(6003, "Mã khuyến mãi đã tồn tại", HttpStatus.CONFLICT),
    CATEGORY_PROMOTION_EXISTED(6004, "Category đã được gán khuyến mãi này", HttpStatus.CONFLICT),
    CATEGORY_PROMOTION_NOT_FOUND(6005, "Không tìm thấy gán khuyến mãi", HttpStatus.NOT_FOUND);

    // ===================== CONSTRUCTOR =====================

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}