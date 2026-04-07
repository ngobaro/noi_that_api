package com.baro.noi_that_api.module.customers.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleAuthRequest {

    @NotBlank(message = "Google ID không được để trống")
    private String idGoogle;

    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Tên không được để trống")
    private String name;
}