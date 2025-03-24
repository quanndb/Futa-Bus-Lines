package com.fasfood.iamservice.application.service.query;


import com.fasfood.common.dto.PageDTO;
import com.fasfood.iamservice.application.dto.request.AccountPagingRequest;
import com.fasfood.iamservice.application.dto.response.CreateOrUpdateAccountResponse;
import com.fasfood.iamservice.application.dto.response.UpdateAccountResponse;

import java.util.UUID;

public interface AccountQueryService {
    PageDTO<UpdateAccountResponse> getAccounts(AccountPagingRequest request);

    CreateOrUpdateAccountResponse getById(UUID id);

    UpdateAccountResponse getMyProfile();
}
