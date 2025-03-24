package com.fasfood.iamservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.CreateOrUpdateRoleRequest;
import com.fasfood.iamservice.application.dto.request.RolePagingRequest;
import com.fasfood.iamservice.application.dto.response.RoleDTO;
import com.fasfood.iamservice.application.service.cmd.RoleCommandService;
import com.fasfood.iamservice.application.service.query.RoleQueryService;
import com.fasfood.iamservice.presentation.rest.RoleController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RoleControllerImpl implements RoleController {

    private final RoleCommandService cmdService;
    private final RoleQueryService queryService;

    @Override
    public PagingResponse<RoleDTO> getRoles(RolePagingRequest request) {
        return PagingResponse.of(this.queryService.getRoles(request));
    }

    @Override
    public Response<RoleDTO> createRole(CreateOrUpdateRoleRequest request) {
        return Response.of(this.cmdService.create(request));
    }

    @Override
    public Response<RoleDTO> updateRole(CreateOrUpdateRoleRequest request, UUID id) {
        return Response.of(this.cmdService.update(id, request));
    }

    @Override
    public Response<RoleDTO> getDetails(UUID id) {
        return Response.of(this.queryService.getRoleDetails(id));
    }

    @Override
    public Response<Void> delete(UUID id) {
        this.cmdService.delete(id);
        return Response.ok();
    }
}
