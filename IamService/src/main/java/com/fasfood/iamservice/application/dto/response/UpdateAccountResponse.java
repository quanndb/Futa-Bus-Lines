package com.fasfood.iamservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.common.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UpdateAccountResponse extends AuditableDTO {
    private String id;
    private String email;
    private String avatarUrl;
    private String fullName;
    private Gender gender;
    private String phoneNumber;
}
