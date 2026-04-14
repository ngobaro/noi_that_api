package com.baro.noi_that_api.module.customers.controller;

import com.baro.noi_that_api.common.dto.ApiResponse;
import com.baro.noi_that_api.module.customers.dto.request.ChangePasswordRequest;
import com.baro.noi_that_api.module.customers.dto.request.CustomerRegisterRequest;
import com.baro.noi_that_api.module.customers.dto.request.CustomerUpdateRequest;
import com.baro.noi_that_api.module.customers.dto.request.GoogleAuthRequest;
import com.baro.noi_that_api.module.customers.dto.response.CustomerResponse;
import com.baro.noi_that_api.module.customers.entity.Customer;
import com.baro.noi_that_api.module.customers.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ApiResponse<CustomerResponse> register(
            @Valid @RequestBody CustomerRegisterRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .code(201)
                .message("Đăng ký thành công")
                .result(customerService.register(request))
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse<Customer> verifyLogin(
            @RequestParam String email,
            @RequestParam String password) {
        return ApiResponse.<Customer>builder()
                .code(200)
                .message("Xác thực thành công")
                .result(customerService.verifyLogin(email, password))
                .build();
    }

    @PostMapping("/google")
    public ApiResponse<Customer> loginGoogle(
            @Valid @RequestBody GoogleAuthRequest request) {
        return ApiResponse.<Customer>builder()
                .code(200)
                .message("Xác thực Google thành công")
                .result(customerService.loginGoogle(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerResponse> getById(@PathVariable Integer id) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .result(customerService.getById(id))
                .build();
    }

    @GetMapping("/email/{email}")
    public ApiResponse<CustomerResponse> getByEmail(@PathVariable String email) {
        CustomerResponse customer = customerService.getByEmail(email);
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .result(customer)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerUpdateRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .result(customerService.update(id, request))
                .build();
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<Void> changePassword(
            @PathVariable Integer id,
            @Valid @RequestBody ChangePasswordRequest request) {
        customerService.changePassword(id, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Đổi mật khẩu thành công")
                .build();
    }

    @PutMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivate(@PathVariable Integer id) {
        customerService.deactivate(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Vô hiệu hóa thành công")
                .build();
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(
            @PathVariable Integer id,
            @RequestParam Boolean isActive) {
        customerService.updateStatus(id, isActive);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Cập nhật trạng thái thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAll() {
        return ApiResponse.<List<CustomerResponse>>builder()
                .code(200)
                .result(customerService.getAll())
                .build();
    }
}