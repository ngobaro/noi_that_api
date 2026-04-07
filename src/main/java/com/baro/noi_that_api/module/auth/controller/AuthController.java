package com.baro.noi_that_api.module.auth.controller;

import com.baro.noi_that_api.common.dto.ApiResponse;
import com.baro.noi_that_api.module.auth.dto.request.ForgotPasswordRequest;
import com.baro.noi_that_api.module.auth.dto.request.LoginRequest;
import com.baro.noi_that_api.module.auth.dto.request.ResetPasswordRequest;
import com.baro.noi_that_api.module.auth.dto.request.VerifyOtpRequest;
import com.baro.noi_that_api.module.auth.dto.response.LoginResponse;
import com.baro.noi_that_api.module.auth.service.AuthService;
import com.baro.noi_that_api.module.customers.dto.request.GoogleAuthRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Đăng nhập thành công")
                .result(authService.login(request))
                .build();
    }

//    @PostMapping("/google")
//    public ApiResponse<LoginResponse> googleLogin(
//            @Valid @RequestBody GoogleAuthRequest request) {
//        return ApiResponse.<LoginResponse>builder()
//                .code(200)
//                .message("Đăng nhập Google thành công")
//                .result(authService.customerGoogleLogin(request))
//                .build();
//    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("OTP đã được gửi đến email của bạn")
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("OTP hợp lệ")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Đặt lại mật khẩu thành công")
                .build();
    }
}