package com.fasfood.iamservice.application.service.query;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.iamservice.application.dto.request.PermissionPagingRequest;
import com.fasfood.iamservice.application.dto.response.PermissionDTO;

public interface PermissionQueryService {
    PageDTO<PermissionDTO> getPermissions(PermissionPagingRequest request);
}
