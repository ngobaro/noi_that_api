package com.baro.noi_that_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    SUCCESS(200, "Thành công", HttpStatus.OK),
    USER_NOT_FOUND(404, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401, "Chưa đăng nhập hoặc token không hợp lệ", HttpStatus.UNAUTHORIZED),
    WRONG_PASSWORD(401, "Mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    GOOGLE_LOGIN_FAILED(401, "Đăng nhập Google thất bại", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    USER_EXISTED(400, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(400, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(500, "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCOUNT_LOCKED(1003, "Tài khoản đã bị khóa", HttpStatus.FORBIDDEN),
    EMAIL_EXISTED(409, "Email đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    INVALID_OTP(409, "Email đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    CATEGORY_EXISTED(409, "Danh mục đã tồn tại", HttpStatus.CONFLICT),
    PRODUCT_NOT_FOUND(404, "Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(404, "Sản phẩm đã tồn tại", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(404, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND);


    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}