package com.fasfood.iamservice.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class CreateOrUpdateAccountResponse extends UpdateAccountResponse {
    private List<AccountRoleResponse> roles;
    private List<String> grantedPermissions;
}
