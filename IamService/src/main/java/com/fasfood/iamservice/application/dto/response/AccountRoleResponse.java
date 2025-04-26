package com.fasfood.iamservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccountRoleResponse extends AuditableDTO {
    private UUID roleId;
    private String name;
    private Boolean isRoot;
}
