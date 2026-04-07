package com.baro.noi_that_api.module.customers.mapper;

import com.baro.noi_that_api.module.customers.dto.request.CustomerRegisterRequest;
import com.baro.noi_that_api.module.customers.dto.request.CustomerUpdateRequest;
import com.baro.noi_that_api.module.customers.dto.response.CustomerResponse;
import com.baro.noi_that_api.module.customers.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idGoogle", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CustomerRegisterRequest request);

    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "idGoogle", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(CustomerUpdateRequest request, @MappingTarget Customer customer);
}