package com.fasfood.iamservice.application.service.cmd;

import com.fasfood.iamservice.application.dto.request.ChangePasswordRequest;
import com.fasfood.iamservice.application.dto.request.CreateAccountRequest;
import com.fasfood.iamservice.application.dto.request.RegisterRequest;
import com.fasfood.iamservice.application.dto.request.SetPasswordRequest;
import com.fasfood.iamservice.application.dto.request.UpdateAccountRequest;
import com.fasfood.iamservice.application.dto.request.UpdateProfileRequest;
import com.fasfood.iamservice.application.dto.response.CreateOrUpdateAccountResponse;
import com.fasfood.iamservice.application.dto.response.UpdateAccountResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface AccountCommandService {

    void register(RegisterRequest request);

    void changePassword(ChangePasswordRequest request);

    void forgotPassword(String email) throws JsonProcessingException;

    void setPassword(SetPasswordRequest request);

    UpdateAccountResponse updateProfile(UpdateProfileRequest request);

    UpdateAccountResponse updateAvatar(MultipartFile image) throws IOException;

    CreateOrUpdateAccountResponse create(CreateAccountRequest request);

    CreateOrUpdateAccountResponse update(UUID id, UpdateAccountRequest request);

    void delete(UUID id);
}
