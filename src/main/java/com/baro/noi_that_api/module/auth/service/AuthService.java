package com.baro.noi_that_api.module.auth.service;

import com.baro.noi_that_api.module.auth.dto.request.ForgotPasswordRequest;
import com.baro.noi_that_api.module.auth.dto.request.LoginRequest;
import com.baro.noi_that_api.module.auth.dto.request.ResetPasswordRequest;
import com.baro.noi_that_api.module.auth.dto.request.VerifyOtpRequest;
import com.baro.noi_that_api.module.auth.dto.response.LoginResponse;
import com.baro.noi_that_api.module.customers.dto.request.GoogleAuthRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);                  // chung cho cả customer + staff
//    LoginResponse customerGoogleLogin(GoogleAuthRequest request);

    void forgotPassword(ForgotPasswordRequest request);         // chung — tự detect customer hay staff
    void verifyOtp(VerifyOtpRequest request);
    void resetPassword(ResetPasswordRequest request);           // chung — tự detect customer hay staff
}