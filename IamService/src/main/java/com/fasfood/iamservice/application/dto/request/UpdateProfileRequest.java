package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest extends Request {
    @NotBlank(message = "FULL_NAME_REQUIRED")
    private String fullName;
    private Gender gender;
    private String phoneNumber;
}
