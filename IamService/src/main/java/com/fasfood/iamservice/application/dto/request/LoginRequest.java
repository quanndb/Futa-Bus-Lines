package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest extends Request {
    @NotBlank(message = "EMAIL_REQUIRED")
    private String email;
    @NotBlank(message = "PASSWORD_REQUIRED")
    private String password;
}
