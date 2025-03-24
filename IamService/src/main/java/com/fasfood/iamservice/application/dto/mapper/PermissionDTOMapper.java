package com.fasfood.iamservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.iamservice.application.dto.response.PermissionDTO;
import com.fasfood.iamservice.domain.Permission;
import com.fasfood.iamservice.infrastructure.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionDTOMapper extends DTOMapper<PermissionDTO, Permission, PermissionEntity> {
}
