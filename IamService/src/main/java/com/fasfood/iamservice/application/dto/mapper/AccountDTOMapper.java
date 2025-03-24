package com.fasfood.iamservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.iamservice.application.dto.response.CreateOrUpdateAccountResponse;
import com.fasfood.iamservice.application.dto.response.UpdateAccountResponse;
import com.fasfood.iamservice.domain.Account;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountDTOMapper extends DTOMapper<CreateOrUpdateAccountResponse, Account, AccountEntity> {
    UpdateAccountResponse from(Account account);

    List<UpdateAccountResponse> from(List<AccountEntity> accounts);
}
