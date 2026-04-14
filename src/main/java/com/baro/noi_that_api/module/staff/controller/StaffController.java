package com.baro.noi_that_api.module.staff.controller;

import com.baro.noi_that_api.common.dto.ApiResponse;
import com.baro.noi_that_api.module.staff.dto.request.ChangePasswordRequest;
import com.baro.noi_that_api.module.staff.dto.request.StaffCreateRequest;
import com.baro.noi_that_api.module.staff.dto.request.StaffUpdateRequest;
import com.baro.noi_that_api.module.staff.dto.response.StaffResponse;
import com.baro.noi_that_api.module.staff.entity.Staff;
import com.baro.noi_that_api.module.staff.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping("/verify")
    public ApiResponse<Staff> verifyLogin(
            @RequestParam String email,
            @RequestParam String password) {
        return ApiResponse.<Staff>builder()
                .code(200)
                .message("Xác thực thành công")
                .result(staffService.verifyLogin(email, password))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<StaffResponse> getById(@PathVariable Integer id) {

        return ApiResponse.<StaffResponse>builder()
                .code(200)
                .result(staffService.getById(id))
                .build();
    }

    @GetMapping("/email/{email}")
    public ApiResponse<StaffResponse> getByEmail(@PathVariable String email) {

        return ApiResponse.<StaffResponse>builder()
                .code(200)
                .result(staffService.getByEmail(email))
                .build();
    }

    @GetMapping
    public ApiResponse<List<StaffResponse>> getAll() {
        return ApiResponse.<List<StaffResponse>>builder()
                .code(200)
                .result(staffService.getAll())
                .build();
    }

    @PostMapping
    public ApiResponse<StaffResponse> create(
            @Valid @RequestBody StaffCreateRequest request) {
        return ApiResponse.<StaffResponse>builder()
                .code(201)
                .message("Tạo staff thành công")
                .result(staffService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<StaffResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody StaffUpdateRequest request) {
        return ApiResponse.<StaffResponse>builder()
                .code(200)
                .result(staffService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        staffService.delete(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa staff thành công")
                .build();
    }

    @PutMapping("/{id}/activate")
    public ApiResponse<Void> activate(@PathVariable Integer id) {
        staffService.activate(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Kích hoạt thành công")
                .build();
    }

    @PutMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivate(@PathVariable Integer id) {
        staffService.deactivate(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Vô hiệu hóa thành công")
                .build();
    }

    @PutMapping("/{id}/password")
    public ApiResponse<Void> updatePassword(
            @PathVariable Integer id,
            @RequestParam String newPassword) {
        staffService.updatePassword(id, newPassword);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Cập nhật mật khẩu thành công")
                .build();
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<Void> changePassword(
            @PathVariable Integer id,
            @Valid @RequestBody ChangePasswordRequest request) {
        staffService.changePassword(id, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Đổi mật khẩu thành công")
                .build();
    }
}