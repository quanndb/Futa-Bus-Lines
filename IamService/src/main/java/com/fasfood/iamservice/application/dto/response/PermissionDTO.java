package com.fasfood.iamservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class PermissionDTO extends AuditableDTO {
    private UUID id;
    private String name;
    private String code;
    private String description;
}
