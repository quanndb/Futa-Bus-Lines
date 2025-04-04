package com.fasfood.iamservice.presentation.rest;

import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.LoginRequest;
import com.fasfood.iamservice.application.dto.request.LogoutRequest;
import com.fasfood.iamservice.application.dto.response.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Auth resource")
@RequestMapping("/api/v1/auth")
@Validated
public interface AuthController {

    @Operation(summary = "Login")
    @PostMapping("/login")
    Response<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) throws JsonProcessingException;

    @Operation(summary = "Login with google")
    @GetMapping("/provider/google")
    Response<LoginResponse> loginWithGoogle(@RequestParam(name = "code") String code) throws JsonProcessingException;

    @Operation(summary = "Verify MAC address")
    @PostMapping("/action/mac-verification")
    Response<Void> verifyMac();

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    Response<Void> logout(@RequestBody @Valid LogoutRequest loginRequest);

    @Operation(summary = "Get new token")
    @PostMapping("/token")
    Response<LoginResponse> getNewToken();

    @Operation(summary = "Get client token")
    @PostMapping(value = "/client-token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Response<ClientResponse> getNewToken(@RequestBody @Valid ClientRequest clientRequest);
}
