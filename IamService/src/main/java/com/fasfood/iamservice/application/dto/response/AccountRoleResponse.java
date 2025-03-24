package com.fasfood.iamservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class AccountRoleResponse extends AuditableDTO {
    private UUID roleId;
    private String name;
    private Boolean isRoot;
}
