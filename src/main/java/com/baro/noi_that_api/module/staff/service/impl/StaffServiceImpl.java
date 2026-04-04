package com.baro.noi_that_api.module.staff.service.impl;

import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.staff.dto.request.ChangePasswordRequest;
import com.baro.noi_that_api.module.staff.dto.request.StaffCreateRequest;
import com.baro.noi_that_api.module.staff.dto.request.StaffUpdateRequest;
import com.baro.noi_that_api.module.staff.dto.response.StaffResponse;
import com.baro.noi_that_api.module.staff.entity.Staff;
import com.baro.noi_that_api.module.staff.mapper.StaffMapper;
import com.baro.noi_that_api.module.staff.repository.StaffRepository;
import com.baro.noi_that_api.module.staff.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Staff verifyLogin(String email, String password) {
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!staff.getIsActive()) {
            throw new AppException(ErrorCode.USER_INACTIVE);
        }

        if (!passwordEncoder.matches(password, staff.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        return staff;
    }

    @Override
    public StaffResponse getById(Integer id) {
        Staff staff = findById(id);
        return staffMapper.toResponse(staff);
    }

    @Override
    public StaffResponse getByEmail(String email) {
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return staffMapper.toResponse(staff);
    }

    @Override
    public void updatePassword(Integer id, String newPassword) {
        Staff staff = findById(id);
        staff.setPassword(passwordEncoder.encode(newPassword));
        staffRepository.save(staff);
    }

    @Override
    public List<StaffResponse> getAll() {
        return staffRepository.findAll()
                .stream()
                .map(staffMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StaffResponse create(StaffCreateRequest request) {
        if (staffRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Staff staff = staffMapper.toEntity(request);
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setIsActive(true);

        return staffMapper.toResponse(staffRepository.save(staff));
    }

    @Override
    public StaffResponse update(Integer id, StaffUpdateRequest request) {
        Staff staff = findById(id);
        staffMapper.updateEntity(request, staff);
        return staffMapper.toResponse(staffRepository.save(staff));
    }

    @Override
    public void delete(Integer id) {
        Staff staff = findById(id);
        staffRepository.delete(staff);
    }

    @Override
    public void activate(Integer id) {
        Staff staff = findById(id);
        staff.setIsActive(true);
        staffRepository.save(staff);
    }

    @Override
    public void deactivate(Integer id) {
        Staff staff = findById(id);
        staff.setIsActive(false);
        staffRepository.save(staff);
    }

    @Override
    public void changePassword(Integer id, ChangePasswordRequest request) {
        Staff staff = findById(id);

        if (!passwordEncoder.matches(request.getOldPassword(), staff.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        staff.setPassword(passwordEncoder.encode(request.getNewPassword()));
        staffRepository.save(staff);
    }

    // ==================== PRIVATE ====================

    private Staff findById(Integer id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}