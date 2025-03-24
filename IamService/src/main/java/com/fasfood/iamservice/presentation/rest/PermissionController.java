package com.fasfood.iamservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.CreateOrUpdatePermissionRequest;
import com.fasfood.iamservice.application.dto.request.PermissionPagingRequest;
import com.fasfood.iamservice.application.dto.response.PermissionDTO;
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

@Tag(name = "Permission resource")
@RequestMapping("/api/v1/permissions")
@Validated
public interface PermissionController {

    @Operation(summary = "Get permissions")
    @GetMapping("")
    PagingResponse<PermissionDTO> getPermissions(@ParameterObject PermissionPagingRequest request);

    @Operation(summary = "Create permissions")
    @PostMapping("")
    Response<PermissionDTO> createPermission(@RequestBody CreateOrUpdatePermissionRequest request);

    @Operation(summary = "Update permissions")
    @PutMapping("/{id}")
    Response<PermissionDTO> updatePermission(@RequestBody CreateOrUpdatePermissionRequest request, @PathVariable UUID id);

    @Operation(summary = "Delete permissions")
    @DeleteMapping("/{id}")
    Response<Void> delete(@PathVariable UUID id);
}
