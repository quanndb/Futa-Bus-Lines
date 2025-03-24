package com.fasfood.iamservice.domain;

import com.fasfood.common.domain.AuditableDomain;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.iamservice.domain.cmd.AssignOrUnassignPermissionCmd;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdateRoleCmd;
import com.fasfood.iamservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.util.IdUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Role extends AuditableDomain {
    private UUID id;
    private Boolean deleted;
    private String name;
    private String description;
    private Set<RolePermission> permissions;
    private Boolean isRoot;

    // domain logic
    public Role(CreateOrUpdateRoleCmd cmd) {
        this.id = IdUtils.nextId();
        this.name = cmd.getName().toLowerCase();
        this.description = cmd.getDescription();
        this.deleted = Boolean.FALSE;
        this.permissions = this.createPermissions(cmd.getPermissions());
        this.isRoot = cmd.getIsRoot();
    }

    public void updateRole(CreateOrUpdateRoleCmd cmd) {
        this.name = cmd.getName();
        this.description = cmd.getDescription();
        this.isRoot = cmd.getIsRoot();
        this.updateRolePermissions(cmd.getPermissions());
    }

    private void updateRolePermissions(Set<AssignOrUnassignPermissionCmd> cmd) {
        if (CollectionUtils.isEmpty(cmd)) {
            throw new ResponseException(BadRequestError.PERMISSION_REQUIRED);
        }
        Map<String, AssignOrUnassignPermissionCmd> permissionMap = cmd.stream()
                .collect(Collectors.toMap(
                        c -> String.join(".", this.id.toString(), c.getPermissionId().toString(), c.getScope().toString()),
                        Function.identity()
                ));

        this.permissions.forEach(permission -> {
            String currentKey = String.join(".", this.id.toString(), permission.getPermissionId().toString(), permission.getScope().toString());
            if (!permissionMap.containsKey(currentKey)) {
                permission.delete();
            }
            permissionMap.remove(currentKey);
        });
        this.permissions.addAll(this.createPermissions(new HashSet<>(permissionMap.values())));
    }

    public void delete() {
        this.deleted = Boolean.TRUE;
    }

    // util
    private Set<RolePermission> createPermissions(Set<AssignOrUnassignPermissionCmd> permissions) {
        if (!CollectionUtils.isEmpty(permissions)) {
            return permissions.stream()
                    .map(permission
                            -> new RolePermission(this.id, permission.getPermissionId(), permission.getScope()))
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public void enrichPermissions(Set<RolePermission> permissions) {
        this.permissions = permissions;
    }
}
