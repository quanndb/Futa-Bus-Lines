package com.fasfood.iamservice.domain;

import com.fasfood.common.domain.AuditableDomain;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdatePermissionCmd;
import com.fasfood.util.IdUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Permission extends AuditableDomain {
    private UUID id;
    private Boolean deleted;
    private String name;
    private String code;
    private String description;

    public Permission(CreateOrUpdatePermissionCmd cmd) {
        this.id = IdUtils.nextId();
        this.deleted = Boolean.FALSE;
        this.name = cmd.getName();
        this.code = cmd.getCode().toLowerCase();
        this.description = cmd.getDescription();
    }

    public void update(CreateOrUpdatePermissionCmd cmd) {
        this.name = cmd.getName();
        this.code = cmd.getCode();
        this.description = cmd.getDescription();
    }

    public void delete() {
        this.deleted = Boolean.TRUE;
    }
}
