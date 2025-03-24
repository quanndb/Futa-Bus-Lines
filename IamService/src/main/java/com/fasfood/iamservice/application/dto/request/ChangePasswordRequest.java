package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest extends Request {
    @NotBlank(message = "OLD_PASSWORD_REQUIRED")
    private String oldPassword;
    @Size(min = 8, message = "PASSWORD_AT_LEAST")
    @NotBlank(message = "NEW_PASSWORD_REQUIRED")
    private String newPassword;
}
