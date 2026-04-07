package com.baro.noi_that_api.exception;

import com.baro.noi_that_api.common.dto.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi phân quyền từ @PreAuthorize
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<?> handleAccessDenied(AccessDeniedException ex) {
        return
                ApiResponse.builder()
                        .code(403)
                        .message("Bạn không có quyền thực hiện thao tác này. Vui lòng kiểm tra quyền tài khoản (ROLE_STAFF hoặc ROLE_ADMIN).")
                        .build();
    }

    // Xử lý AppException (lỗi business của bạn)
    @ExceptionHandler(AppException.class)
    public ApiResponse<?> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(ex.getMessage() != null ? ex.getMessage() : errorCode.getMessage())
                        .build();
    }

    // Xử lý các lỗi chưa biết khác (debug)
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception ex) {
        ex.printStackTrace();   // ← Quan trọng: in lỗi ra console để xem nguyên nhân thật

        return ApiResponse.builder()
                        .code(500)
                        .message("Lỗi hệ thống: " + ex.getMessage())
                        .build();
    }
}

//package com.baro.noithat_api.exception;
//
//import com.baro.noithat_api.common.dto.ApiResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    // ❌ AppException
//    @ExceptionHandler(AppException.class)
//    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
//        ErrorCode errorCode = ex.getErrorCode();
//
//        ApiResponse<?> response = ApiResponse.builder()
//                .code(errorCode.getCode())
//                .message(errorCode.getMessage())
//                .build();
//
//        return ResponseEntity
//                .status(errorCode.getStatus())
//                .body(response);
//    }
//
//    // ❌ Exception chung
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
//        ApiResponse<?> response = ApiResponse.builder()
//                .code(500)
//                .message("Internal server error")
//                .build();
//
//        return ResponseEntity
//                .internalServerError()
//                .body(response);
//    }
//}

