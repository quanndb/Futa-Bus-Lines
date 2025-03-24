package com.fasfood.iamservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.CreateOrUpdatePermissionRequest;
import com.fasfood.iamservice.application.dto.request.PermissionPagingRequest;
import com.fasfood.iamservice.application.dto.response.PermissionDTO;
import com.fasfood.iamservice.application.service.cmd.PermissionCommandService;
import com.fasfood.iamservice.application.service.query.PermissionQueryService;
import com.fasfood.iamservice.presentation.rest.PermissionController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PermissionControllerImpl implements PermissionController {

    private final PermissionCommandService cmdService;
    private final PermissionQueryService queryService;

    @Override
    public PagingResponse<PermissionDTO> getPermissions(PermissionPagingRequest request) {
        return PagingResponse.of(this.queryService.getPermissions(request));
    }

    @Override
    public Response<PermissionDTO> createPermission(CreateOrUpdatePermissionRequest request) {
        return Response.of(this.cmdService.create(request));
    }

    @Override
    public Response<PermissionDTO> updatePermission(CreateOrUpdatePermissionRequest request, UUID id) {
        return Response.of(this.cmdService.update(id, request));
    }

    @Override
    public Response<Void> delete(UUID id) {
        this.cmdService.delete(id);
        return Response.ok();
    }
}
