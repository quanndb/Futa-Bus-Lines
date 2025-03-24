package com.fasfood.iamservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.iamservice.domain.RolePermission;
import com.fasfood.iamservice.infrastructure.persistence.entity.RolePermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface RolePermissionEntityMapper extends EntityMapper<RolePermission, RolePermissionEntity> {
}
