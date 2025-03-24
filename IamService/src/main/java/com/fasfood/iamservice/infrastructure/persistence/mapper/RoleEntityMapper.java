package com.fasfood.iamservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.iamservice.domain.Role;
import com.fasfood.iamservice.infrastructure.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface RoleEntityMapper extends EntityMapper<Role, RoleEntity> {
}
