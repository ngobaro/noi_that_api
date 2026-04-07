package com.baro.noi_that_api.module.staff.service;

import com.baro.noi_that_api.module.staff.dto.request.ChangePasswordRequest;
import com.baro.noi_that_api.module.staff.dto.request.StaffCreateRequest;
import com.baro.noi_that_api.module.staff.dto.request.StaffUpdateRequest;
import com.baro.noi_that_api.module.staff.dto.response.StaffResponse;
import com.baro.noi_that_api.module.staff.entity.Staff;

import java.util.List;

public interface StaffService {

    // Internal - Node.js gọi
    Staff verifyLogin(String email, String password);
    StaffResponse getById(Integer id);
    StaffResponse getByEmail(String email);
    void updatePassword(Integer id, String newPassword);

    // Admin CRUD
    List<StaffResponse> getAll();
    StaffResponse create(StaffCreateRequest request);
    StaffResponse update(Integer id, StaffUpdateRequest request);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);

    // Staff tự đổi password
    void changePassword(Integer id, ChangePasswordRequest request);
}