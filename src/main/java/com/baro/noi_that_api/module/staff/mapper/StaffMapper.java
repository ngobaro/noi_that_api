package com.baro.noi_that_api.module.staff.mapper;

import com.baro.noi_that_api.module.staff.dto.request.StaffCreateRequest;
import com.baro.noi_that_api.module.staff.dto.request.StaffUpdateRequest;
import com.baro.noi_that_api.module.staff.dto.response.StaffResponse;
import com.baro.noi_that_api.module.staff.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "idGoogle", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Staff toEntity(StaffCreateRequest request);

    StaffResponse toResponse(Staff staff);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "idGoogle", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(StaffUpdateRequest request, @MappingTarget Staff staff);
}