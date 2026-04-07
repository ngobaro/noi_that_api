package com.baro.noi_that_api.module.customers.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateRequest {

    @NotBlank(message = "Tên không được để trống")
    private String name;
}