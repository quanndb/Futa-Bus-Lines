package com.fasfood.iamservice.domain.cmd;

import com.fasfood.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateOrUpdateAccountCmd {
    private String email;
    private String password;
    private String fullName;
    private Gender gender;
    private String phoneNumber;
    private Set<UUID> roleIds;
}
