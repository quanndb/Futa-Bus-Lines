package com.fasfood.iamservice.domain;

import com.fasfood.common.domain.AuditableDomain;
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
public class AccountRole extends AuditableDomain {
    private UUID id;
    private Boolean deleted;
    private UUID accountId;
    private UUID roleId;

    public AccountRole(UUID accountId, UUID roleId) {
        this.id = IdUtils.nextId();
        this.accountId = accountId;
        this.roleId = roleId;
        this.deleted = Boolean.FALSE;
    }

    public void delete() {
        this.deleted = Boolean.TRUE;
    }
}
