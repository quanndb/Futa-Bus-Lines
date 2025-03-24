package com.fasfood.iamservice.presentation.rest;

import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.LoginRequest;
import com.fasfood.iamservice.application.dto.request.LogoutRequest;
import com.fasfood.iamservice.application.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth resource")
@RequestMapping("/api/v1/auth")
@Validated
public interface AuthController {

    @Operation(summary = "Login")
    @PostMapping("/login")
    Response<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    Response<Void> logout(@RequestBody @Valid LogoutRequest loginRequest);

    @Operation(summary = "Get new token")
    @PostMapping("/token")
    Response<LoginResponse> getNewToken();
}
