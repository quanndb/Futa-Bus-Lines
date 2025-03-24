package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateOrUpdateRoleRequest extends Request {
    @NotBlank(message = "ROLE_NAME_REQUIRED")
    private String name;
    private String description;
    @NotNull(message = "IS_ROOT_REQUIRED")
    private Boolean isRoot;
    @NotEmpty(message = "PERMISSION_REQUIRED")
    private Set<AssignOrUnassignRolePermissionRequest> permissions;
}
