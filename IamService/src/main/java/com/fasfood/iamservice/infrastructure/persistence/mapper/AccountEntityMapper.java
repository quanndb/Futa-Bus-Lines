package com.fasfood.iamservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.iamservice.domain.Account;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface AccountEntityMapper extends EntityMapper<Account, AccountEntity> {
}
