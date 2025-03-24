package com.fasfood.iamservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.iamservice.application.dto.response.RoleDTO;
import com.fasfood.iamservice.domain.Role;
import com.fasfood.iamservice.infrastructure.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleDTOMapper extends DTOMapper<RoleDTO, Role, RoleEntity> {
}
