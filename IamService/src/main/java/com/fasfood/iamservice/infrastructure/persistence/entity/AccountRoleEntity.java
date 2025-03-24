package com.fasfood.iamservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.validator.ValidateConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "account_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRoleEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    @Column(name = "account_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID accountId;
    @Column(name = "role_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID roleId;
}
