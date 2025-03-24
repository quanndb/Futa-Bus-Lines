package com.fasfood.iamservice.application.mapper;

import com.fasfood.iamservice.application.dto.request.CreateAccountRequest;
import com.fasfood.iamservice.application.dto.request.CreateOrUpdatePermissionRequest;
import com.fasfood.iamservice.application.dto.request.CreateOrUpdateRoleRequest;
import com.fasfood.iamservice.application.dto.request.RegisterRequest;
import com.fasfood.iamservice.application.dto.request.UpdateAccountRequest;
import com.fasfood.iamservice.application.dto.request.UpdateProfileRequest;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdateAccountCmd;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdatePermissionCmd;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdateRoleCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IamCommandMapper {
    CreateOrUpdateAccountCmd from(CreateAccountRequest request);

    CreateOrUpdateAccountCmd from(RegisterRequest request);

    CreateOrUpdateAccountCmd from(UpdateProfileRequest request);

    CreateOrUpdateAccountCmd from(UpdateAccountRequest request);

    CreateOrUpdateRoleCmd from(CreateOrUpdateRoleRequest request);

    CreateOrUpdatePermissionCmd from(CreateOrUpdatePermissionRequest request);
}
