package com.baro.noi_that_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * AppException là exception tùy chỉnh cho toàn bộ ứng dụng.
 * Mục đích:
 * - Mang theo ErrorCode để dễ dàng map ra response thống nhất
 * - Có thể truyền thêm message tùy chỉnh nếu cần (override message mặc định của ErrorCode)
 * - Hỗ trợ stack trace đầy đủ khi debug
 */
@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String customMessage;  // optional: nếu muốn override message

    /**
     * Constructor cơ bản: dùng message từ ErrorCode
     */
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = null;
    }

    /**
     * Constructor nâng cao: override message tùy chỉnh
     */
    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage != null ? customMessage : errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    /**
     * Constructor đầy đủ: hỗ trợ cause (khi wrap exception khác)
     */
    public AppException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage != null ? customMessage : errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    /**
     * Helper để lấy HTTP status từ ErrorCode
     */
    public HttpStatus getHttpStatus() {
        return errorCode.getStatus();
    }

    /**
     * Lấy message cuối cùng (ưu tiên custom nếu có)
     */
    @Override
    public String getMessage() {
        return customMessage != null ? customMessage : errorCode.getMessage();
    }
}