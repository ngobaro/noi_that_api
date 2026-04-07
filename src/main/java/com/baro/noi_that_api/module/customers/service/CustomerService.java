package com.baro.noi_that_api.module.customers.service;

import com.baro.noi_that_api.module.customers.dto.request.ChangePasswordRequest;
import com.baro.noi_that_api.module.customers.dto.request.CustomerRegisterRequest;
import com.baro.noi_that_api.module.customers.dto.request.CustomerUpdateRequest;
import com.baro.noi_that_api.module.customers.dto.request.GoogleAuthRequest;
import com.baro.noi_that_api.module.customers.dto.response.CustomerResponse;
import com.baro.noi_that_api.module.customers.entity.Customer;

import java.util.List;

public interface CustomerService {

    // Internal - Node.js gọi
    CustomerResponse register(CustomerRegisterRequest request);
    Customer verifyLogin(String email, String password);
    Customer loginGoogle(GoogleAuthRequest request);
    CustomerResponse getById(Integer id);
    CustomerResponse getByEmail(String email);
    CustomerResponse update(Integer id, CustomerUpdateRequest request);
    void changePassword(Integer id, ChangePasswordRequest request);
    void deactivate(Integer id);

    // Admin
    List<CustomerResponse> getAll();
    void updateStatus(Integer id, Boolean isActive);
}