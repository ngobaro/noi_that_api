package com.baro.noi_that_api.module.staff.dto.response;

import com.baro.noi_that_api.enums.Role;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponse {

    private Integer id;
    private String name;
    private String email;
    private Role type;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}