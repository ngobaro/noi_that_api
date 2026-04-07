package com.baro.noi_that_api.module.auth.service.impl;

import com.baro.noi_that_api.common.email.EmailService;
import com.baro.noi_that_api.common.otp.OtpService;
import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.auth.dto.request.ForgotPasswordRequest;
import com.baro.noi_that_api.module.auth.dto.request.LoginRequest;
import com.baro.noi_that_api.module.auth.dto.request.ResetPasswordRequest;
import com.baro.noi_that_api.module.auth.dto.request.VerifyOtpRequest;
import com.baro.noi_that_api.module.auth.dto.response.LoginResponse;
import com.baro.noi_that_api.module.auth.service.AuthService;
import com.baro.noi_that_api.module.customers.dto.request.GoogleAuthRequest;
import com.baro.noi_that_api.module.customers.entity.Customer;
import com.baro.noi_that_api.module.customers.repository.CustomerRepository;
import com.baro.noi_that_api.module.staff.entity.Staff;
import com.baro.noi_that_api.module.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final int OTP_EXPIRE_MINUTES = 5;

    // ===================== LOGIN CHUNG =====================

    @Override
    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        // Thử tìm trong staff trước
        Optional<Staff> staffOpt = staffRepository.findByEmail(email);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();

            if (!staff.getIsActive()) {
                throw new AppException(ErrorCode.USER_INACTIVE);
            }
            if (!passwordEncoder.matches(password, staff.getPassword())) {
                throw new AppException(ErrorCode.INVALID_PASSWORD);
            }

            return LoginResponse.builder()
                    .id(staff.getId())
                    .name(staff.getName())
                    .email(staff.getEmail())
                    .role(staff.getType().name())   // "Staff" hoặc "Admin"
                    .isActive(staff.getIsActive())
                    .build();
        }

        // Thử tìm trong customer
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();

            if (!customer.getIsActive()) {
                throw new AppException(ErrorCode.USER_INACTIVE);
            }
            if (customer.getPassword() == null) {
                throw new AppException(ErrorCode.PASSWORD_NOT_SET);
            }
            if (!passwordEncoder.matches(password, customer.getPassword())) {
                throw new AppException(ErrorCode.INVALID_PASSWORD);
            }

            return LoginResponse.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .role("CUSTOMER")
                    .isActive(customer.getIsActive())
                    .build();
        }

        // Không tìm thấy ở đâu cả
        throw new AppException(ErrorCode.USER_NOT_FOUND);
    }

    // ===================== GOOGLE =====================

//    @Override
//    public LoginResponse customerGoogleLogin(GoogleAuthRequest request) {
//        // Tìm theo googleId trước
//        Customer customer = customerRepository.findByIdGoogle(request.getIdGoogle())
//                .orElseGet(() ->
//                        customerRepository.findByEmail(request.getEmail())
//                                .map(existing -> {
//                                    existing.setIdGoogle(request.getIdGoogle());
//                                    return customerRepository.save(existing);
//                                })
//                                .orElseGet(() -> {
//                                    Customer newCustomer = Customer.builder()
//                                            .name(request.getName())
//                                            .email(request.getEmail())
//                                            .idGoogle(request.getIdGoogle())
//                                            .isActive(true)
//                                            .build();
//                                    return customerRepository.save(newCustomer);
//                                })
//                );
//
//        if (!customer.getIsActive()) {
//            throw new AppException(ErrorCode.USER_INACTIVE);
//        }
//
//        return LoginResponse.builder()
//                .id(customer.getId())
//                .name(customer.getName())
//                .email(customer.getEmail())
//                .role("CUSTOMER")
//                .isActive(customer.getIsActive())
//                .build();
//    }

    // ===================== FORGOT PASSWORD CHUNG =====================

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail();
        String name;

        // Detect customer hay staff
        Optional<Staff> staffOpt = staffRepository.findByEmail(email);
        if (staffOpt.isPresent()) {
            name = staffOpt.get().getName();
        } else {
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            name = customer.getName();
        }

        if (otpService.hasActiveOtp(email)) {
            throw new AppException(ErrorCode.OTP_ALREADY_SENT);
        }

        String otp = generateOtp();
        otpService.saveOtp(email, otp, OTP_EXPIRE_MINUTES);
        emailService.sendOtpEmail(email, otp, name);
    }

    // ===================== VERIFY OTP =====================

    @Override
    public void verifyOtp(VerifyOtpRequest request) {
        boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (!valid) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }
    }

    // ===================== RESET PASSWORD CHUNG =====================

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();

        // Verify OTP trước
        boolean valid = otpService.verifyOtp(email, request.getOtp());
        if (!valid) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        // Detect customer hay staff rồi cập nhật
        Optional<Staff> staffOpt = staffRepository.findByEmail(email);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.setPassword(encodedPassword);
            staffRepository.save(staff);
        } else {
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            customer.setPassword(encodedPassword);
            customerRepository.save(customer);
        }

        otpService.deleteOtp(email);
    }

    // ===================== PRIVATE =====================

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}