package com.fasfood.iamservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.iamservice.domain.Permission;
import com.fasfood.iamservice.infrastructure.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface PermissionEntityMapper extends EntityMapper<Permission, PermissionEntity> {
}
