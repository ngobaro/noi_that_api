package com.baro.noi_that_api.module.auth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Integer id;
    private String name;
    private String email;
    private String role;
    private Boolean isActive;
}