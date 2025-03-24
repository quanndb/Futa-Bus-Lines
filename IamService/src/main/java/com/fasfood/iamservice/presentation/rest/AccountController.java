package com.fasfood.iamservice.presentation.rest;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "Accounts resource")
@RequestMapping("/api/v1/accounts")
@Validated
public interface AccountController {
    @Operation(summary = "Register")
    @PostMapping("/register")
    Response<Void> register(@RequestBody @Valid RegisterRequest request);

    @Operation(summary = "Change password")
    @PostMapping("/me/password")
    Response<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request);

    @Operation(summary = "Forgot password")
    @PostMapping("/me/forgot-password")
    Response<Void> forgotPassword();

    @Operation(summary = "Forgot password")
    @PostMapping("/me/password-verification")
    Response<Void> verifyPassword(@RequestBody @Valid SetPasswordRequest request);

    @Operation(summary = "Update profile")
    @PatchMapping("/me")
    Response<UpdateAccountResponse> updateProfile(@RequestBody @Valid UpdateProfileRequest request);

    @Operation(summary = "Update avatar")
    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<UpdateAccountResponse> updateAvatar(@RequestPart MultipartFile image) throws IOException;

    @Operation(summary = "Create account")
    @PostMapping("/")
    Response<CreateOrUpdateAccountResponse> createAccount(@RequestBody @Valid CreateAccountRequest request);

    @Operation(summary = "Update account")
    @PostMapping("/{id}")
    Response<CreateOrUpdateAccountResponse> updateAccount(@RequestBody @Valid UpdateAccountRequest request, @PathVariable UUID id);

    @Operation(summary = "Delete account")
    @DeleteMapping("/{id}")
    Response<Void> delete(@PathVariable UUID id);

    @Operation(summary = "Get details")
    @GetMapping("/{id}")
    Response<CreateOrUpdateAccountResponse> getDetails(@PathVariable UUID id);

    @Operation(summary = "Get user authorities")
    @GetMapping("/{id}/authorities")
    Response<UserAuthority> getUserAuthorities(@PathVariable UUID id);

    @Operation(summary = "Get profile")
    @GetMapping("/me")
    Response<UpdateAccountResponse> getMyProfile();

    @Operation(summary = "Get accounts")
    @GetMapping("")
    PagingResponse<UpdateAccountResponse> getAccounts(@ParameterObject AccountPagingRequest request);
}
