package com.fasfood.iamservice.application.service.cmd;

import com.fasfood.iamservice.application.dto.request.CreateOrUpdateRoleRequest;
import com.fasfood.iamservice.application.dto.response.RoleDTO;

import java.util.UUID;

public interface RoleCommandService {
    RoleDTO create(CreateOrUpdateRoleRequest request);

    RoleDTO update(UUID roleId, CreateOrUpdateRoleRequest request);

    void delete(UUID id);
}
