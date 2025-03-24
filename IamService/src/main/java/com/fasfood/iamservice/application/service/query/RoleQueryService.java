package com.fasfood.iamservice.application.service.query;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.iamservice.application.dto.request.RolePagingRequest;
import com.fasfood.iamservice.application.dto.response.RoleDTO;

import java.util.UUID;

public interface RoleQueryService {
    PageDTO<RoleDTO> getRoles(RolePagingRequest request);

    RoleDTO getRoleDetails(UUID id);
}
