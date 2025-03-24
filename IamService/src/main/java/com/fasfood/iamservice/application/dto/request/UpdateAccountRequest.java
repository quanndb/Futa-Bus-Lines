package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UpdateAccountRequest extends Request {
    @Size(min = 8, message = "PASSWORD_AT_LEAST")
    private String password;
    @NotBlank(message = "FULL_NAME_REQUIRED")
    private String fullName;
    private Gender gender;
    private String phoneNumber;
    @NotNull(message = "ROLE_REQUIRED")
    private List<UUID> roleIds;
}
