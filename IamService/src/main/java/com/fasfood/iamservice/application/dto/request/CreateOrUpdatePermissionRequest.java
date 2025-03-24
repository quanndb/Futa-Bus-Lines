package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdatePermissionRequest extends Request {
    @NotBlank(message = "PERMISSION_NAME_REQUIRED")
    private String name;
    @NotBlank(message = "PERMISSION_CODE_REQUIRED")
    private String code;
    private String description;
}
