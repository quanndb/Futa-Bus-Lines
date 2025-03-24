package com.fasfood.iamservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
    @NotBlank(message = "REFRESH_TOKEN_REQUIRED")
    private String refreshToken;
}
