package com.baro.noi_that_api.module.staff.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffUpdateRequest {

    @NotBlank(message = "Tên không được để trống")
    private String name;
}