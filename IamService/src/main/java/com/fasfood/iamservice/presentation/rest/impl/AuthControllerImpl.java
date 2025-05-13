package com.fasfood.iamservice.presentation.rest.impl;

import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.ClientTokenRequest;
import com.fasfood.iamservice.application.dto.request.LoginRequest;
import com.fasfood.iamservice.application.dto.request.LogoutRequest;
import com.fasfood.iamservice.application.dto.response.LoginResponse;
import com.fasfood.iamservice.application.service.AuthService;
import com.fasfood.iamservice.presentation.rest.AuthController;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;
    private final ClientAuthentication clientAuthentication;
    private static final String CLIENT_CREDENTIALS = "client_credentials";

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) throws JsonProcessingException {
        return Response.of(this.authService.login(loginRequest));
    }

    @Override
    public Response<LoginResponse> loginWithGoogle(String code) throws JsonProcessingException {
        return Response.of(this.authService.loginWithGoogle(code));
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
    public ClientResponse getClientToken(ClientTokenRequest request) {
        return this.clientAuthentication.getClientToken(new ClientRequest(request.getClientId(), request.getClientSecret(), CLIENT_CREDENTIALS));
    }
}
