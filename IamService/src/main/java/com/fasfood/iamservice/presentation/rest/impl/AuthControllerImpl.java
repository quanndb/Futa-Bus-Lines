package com.fasfood.iamservice.presentation.rest.impl;

import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.LoginRequest;
import com.fasfood.iamservice.application.dto.request.LogoutRequest;
import com.fasfood.iamservice.application.dto.response.LoginResponse;
import com.fasfood.iamservice.application.service.AuthService;
import com.fasfood.iamservice.application.service.ClientService;
import com.fasfood.iamservice.presentation.rest.AuthController;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;
    private final ClientService clientService;

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) throws JsonProcessingException {
        return Response.of(this.authService.login(loginRequest));
    }

    @Override
    public Response<LoginResponse> loginWithGoogle(String authCode) throws JsonProcessingException {
        return Response.of(this.authService.loginWithGoogle(authCode));
    }

    @Override
    public Response<Void> verifyMac() {
        this.authService.verifyMac();
        return Response.ok();
    }

    @Override
    public Response<Void> logout(LogoutRequest loginRequest) {
        this.authService.logout(loginRequest);
        return Response.ok();
    }

    @Override
    public Response<LoginResponse> getNewToken() {
        return Response.of(this.authService.refresh());
    }

    @Override
    public Response<ClientResponse> getNewToken(ClientRequest clientRequest) {
        return Response.of(this.clientService.getClientToken(clientRequest));
    }
}
