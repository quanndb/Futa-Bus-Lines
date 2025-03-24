package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.common.enums.AccountStatus;
import com.fasfood.common.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AccountPagingRequest extends PagingRequest {
    private List<Gender> gender;
    private List<AccountStatus> status;
}
