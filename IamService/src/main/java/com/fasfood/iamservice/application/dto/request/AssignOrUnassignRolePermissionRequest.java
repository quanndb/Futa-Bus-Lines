package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.common.enums.Scope;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssignOrUnassignRolePermissionRequest extends Request {
    @NotNull(message = "PERMISSION_REQUIRED")
    private UUID permissionId;
    @NotNull(message = "SCOPE_REQUIRED")
    private Scope scope;
}
