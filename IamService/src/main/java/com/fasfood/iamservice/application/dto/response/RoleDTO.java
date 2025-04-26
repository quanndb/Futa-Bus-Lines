package com.fasfood.iamservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.iamservice.domain.RolePermission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RoleDTO extends AuditableDTO {
    private UUID id;
    private String name;
    private String description;
    private List<RolePermission> permissions;
    private Boolean isRoot;
}
