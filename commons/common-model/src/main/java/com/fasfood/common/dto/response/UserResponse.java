package com.fasfood.common.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.common.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends AuditableDTO {
    private String id;
    private String email;
    private String avatarUrl;
    private String fullName;
    private Gender gender;
    private String phoneNumber;
}
