package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest extends Request {
    @Size(min = 8, message = "PASSWORD_AT_LEAST")
    @NotBlank(message = "PASSWORD_REQUIRED")
    private String password;
    @Email
    @NotBlank(message = "EMAIL_REQUIRED")
    private String email;
    @NotBlank(message = "FULL_NAME_REQUIRED")
    private String fullName;
    private Gender gender;
    private String phoneNumber;
}
