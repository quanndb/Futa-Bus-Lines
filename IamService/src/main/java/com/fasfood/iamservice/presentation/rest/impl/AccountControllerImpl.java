package com.fasfood.iamservice.presentation.rest.impl;

import com.fasfood.common.UserAuthority;
import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.iamservice.application.dto.request.AccountPagingRequest;
import com.fasfood.iamservice.application.dto.request.ChangePasswordRequest;
import com.fasfood.iamservice.application.dto.request.CreateAccountRequest;
import com.fasfood.iamservice.application.dto.request.RegisterRequest;
import com.fasfood.iamservice.application.dto.request.SetPasswordRequest;
import com.fasfood.iamservice.application.dto.request.UpdateAccountRequest;
import com.fasfood.iamservice.application.dto.request.UpdateProfileRequest;
import com.fasfood.iamservice.application.dto.response.CreateOrUpdateAccountResponse;
import com.fasfood.iamservice.application.dto.response.UpdateAccountResponse;
import com.fasfood.iamservice.application.service.cmd.AccountCommandService;
import com.fasfood.iamservice.application.service.query.AccountQueryService;
import com.fasfood.iamservice.presentation.rest.AccountController;
import com.fasfood.web.security.AuthorityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final AccountCommandService accountCommandService;
    private final AccountQueryService accountQueryService;
    private final AuthorityService authorityService;

    @Override
    public Response<Void> register(RegisterRequest request) {
        this.accountCommandService.register(request);
        return Response.ok();
    }

    @Override
    public Response<Void> changePassword(ChangePasswordRequest request) {
        this.accountCommandService.changePassword(request);
        return Response.ok();
    }

    @Override
    public Response<Void> forgotPassword(String email) throws JsonProcessingException {
        this.accountCommandService.forgotPassword(email);
        return Response.ok();
    }

    @Override
    public Response<Void> verifyPassword(SetPasswordRequest request) {
        this.accountCommandService.setPassword(request);
        return Response.ok();
    }

    @Override
    public Response<UpdateAccountResponse> updateProfile(UpdateProfileRequest request) {
        return Response.of(this.accountCommandService.updateProfile(request));
    }

    @Override
    public Response<UpdateAccountResponse> updateAvatar(MultipartFile image) throws IOException {
        return Response.of(this.accountCommandService.updateAvatar(image));
    }

    @Override
    public Response<CreateOrUpdateAccountResponse> createAccount(CreateAccountRequest request) {
        return Response.of(this.accountCommandService.create(request));
    }

    @Override
    public Response<CreateOrUpdateAccountResponse> updateAccount(UpdateAccountRequest request, UUID id) {
        return Response.of(this.accountCommandService.update(id, request));
    }

    @Override
    public Response<Void> setActive(UUID id, Boolean active) {
        this.accountCommandService.setActive(id, active);
        return Response.ok();
    }

    @Override
    public Response<Void> delete(UUID id) {
        this.accountCommandService.delete(id);
        return Response.ok();
    }

    @Override
    public Response<CreateOrUpdateAccountResponse> getDetails(UUID id) {
        return Response.of(this.accountQueryService.getById(id));
    }

    @Override
    public Response<UserAuthority> getUserAuthorities(UUID id) {
        return Response.of(this.authorityService.getUserAuthority(id));
    }

    @Override
    public Response<UpdateAccountResponse> getMyProfile() {
        return Response.of(this.accountQueryService.getMyProfile());
    }

    @Override
    public PagingResponse<UpdateAccountResponse> getAccounts(AccountPagingRequest request) {
        return PagingResponse.of(this.accountQueryService.getAccounts(request));
    }
}
