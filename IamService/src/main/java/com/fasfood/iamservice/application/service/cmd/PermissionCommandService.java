package com.fasfood.iamservice.application.service.cmd;

import com.fasfood.iamservice.application.dto.request.CreateOrUpdatePermissionRequest;
import com.fasfood.iamservice.application.dto.response.PermissionDTO;

import java.util.UUID;

public interface PermissionCommandService {
    PermissionDTO create(CreateOrUpdatePermissionRequest request);

    PermissionDTO update(UUID id, CreateOrUpdatePermissionRequest request);

    void delete(UUID id);
}
