package com.baro.noi_that_api.module.customers.service.impl;

import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import com.baro.noi_that_api.module.customers.dto.request.ChangePasswordRequest;
import com.baro.noi_that_api.module.customers.dto.request.CustomerRegisterRequest;
import com.baro.noi_that_api.module.customers.dto.request.CustomerUpdateRequest;
import com.baro.noi_that_api.module.customers.dto.request.GoogleAuthRequest;
import com.baro.noi_that_api.module.customers.dto.response.CustomerResponse;
import com.baro.noi_that_api.module.customers.entity.Customer;
import com.baro.noi_that_api.module.customers.mapper.CustomerMapper;
import com.baro.noi_that_api.module.customers.repository.CustomerRepository;
import com.baro.noi_that_api.module.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponse register(CustomerRegisterRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setIsActive(true);

        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    public Customer verifyLogin(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!customer.getIsActive()) {
            throw new AppException(ErrorCode.USER_INACTIVE);
        }

        if (customer.getPassword() == null) {
            throw new AppException(ErrorCode.PASSWORD_NOT_SET);
        }

        if (!passwordEncoder.matches(password, customer.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        return customer;
    }

    @Override
    public Customer loginGoogle(GoogleAuthRequest request) {
        // Tìm theo googleId trước
        return customerRepository.findByIdGoogle(request.getIdGoogle())
                .orElseGet(() ->
                        // Chưa có googleId → thử tìm theo email
                        customerRepository.findByEmail(request.getEmail())
                                .map(existing -> {
                                    // Email tồn tại → gán googleId vào
                                    existing.setIdGoogle(request.getIdGoogle());
                                    return customerRepository.save(existing);
                                })
                                .orElseGet(() -> {
                                    // Chưa có gì cả → tạo mới
                                    Customer newCustomer = Customer.builder()
                                            .name(request.getName())
                                            .email(request.getEmail())
                                            .idGoogle(request.getIdGoogle())
                                            .isActive(true)
                                            .build();
                                    return customerRepository.save(newCustomer);
                                })
                );
    }

    @Override
    public CustomerResponse getById(Integer id) {
        return customerMapper.toResponse(findById(id));
    }

    @Override
    public CustomerResponse getByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse update(Integer id, CustomerUpdateRequest request) {
        Customer customer = findById(id);
        customerMapper.updateEntity(request, customer);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    public void changePassword(Integer id, ChangePasswordRequest request) {
        Customer customer = findById(id);

        if (customer.getPassword() == null) {
            throw new AppException(ErrorCode.PASSWORD_NOT_SET);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);
    }

    @Override
    public void deactivate(Integer id) {
        Customer customer = findById(id);
        customer.setIsActive(false);
        customerRepository.save(customer);
    }

    @Override
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Integer id, Boolean isActive) {
        Customer customer = findById(id);
        customer.setIsActive(isActive);
        customerRepository.save(customer);
    }

    // ==================== PRIVATE ====================

    private Customer findById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}