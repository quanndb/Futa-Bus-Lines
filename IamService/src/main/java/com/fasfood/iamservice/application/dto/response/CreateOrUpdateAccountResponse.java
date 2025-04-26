package com.fasfood.iamservice.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrUpdateAccountResponse extends UpdateAccountResponse {
    private List<AccountRoleResponse> roles;
    private List<String> grantedPermissions;
}
