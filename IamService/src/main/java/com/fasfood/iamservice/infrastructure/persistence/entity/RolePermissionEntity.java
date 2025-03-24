package com.fasfood.iamservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.enums.Scope;
import com.fasfood.common.validator.ValidateConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "role_permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    @Column(name = "role_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID roleId;
    @Column(name = "permission_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID permissionId;
    @Column(name = "scope", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private Scope scope;
}
