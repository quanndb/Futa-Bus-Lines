package com.fasfood.iamservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.CreateOrUpdateRoleRequest;
import com.fasfood.iamservice.application.dto.request.RolePagingRequest;
import com.fasfood.iamservice.application.dto.response.RoleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Role resource")
@RequestMapping("/api/v1/roles")
@Validated
public interface RoleController {
    @Operation(summary = "Get roles")
    @GetMapping("")
    PagingResponse<RoleDTO> getRoles(@ParameterObject RolePagingRequest request);

    @Operation(summary = "Create role")
    @PostMapping("")
    Response<RoleDTO> createRole(@RequestBody CreateOrUpdateRoleRequest request);

    @Operation(summary = "Update role")
    @PutMapping("/{id}")
    Response<RoleDTO> updateRole(@RequestBody CreateOrUpdateRoleRequest request, @PathVariable UUID id);

    @Operation(summary = "Get role details")
    @GetMapping("/{id}")
    Response<RoleDTO> getDetails(@PathVariable UUID id);

    @Operation(summary = "Delete role")
    @DeleteMapping("/{id}")
    Response<Void> delete(@PathVariable UUID id);
}
