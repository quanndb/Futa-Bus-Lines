package com.fasfood.iamservice.domain;

import com.fasfood.common.domain.AuditableDomain;
import com.fasfood.common.enums.Scope;
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
public class RolePermission extends AuditableDomain {
    private UUID id;
    private Boolean deleted;
    private UUID roleId;
    private UUID permissionId;
    private Scope scope;

    public RolePermission(UUID roleId, UUID permissionId, Scope scope) {
        this.id = IdUtils.nextId();
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.scope = scope;
        this.deleted = Boolean.FALSE;
    }

    public void delete() {
        this.deleted = Boolean.TRUE;
    }
}
