package com.fasfood.iamservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.iamservice.domain.AccountRole;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface AccountRoleEntityMapper extends EntityMapper<AccountRole, AccountRoleEntity> {
}
