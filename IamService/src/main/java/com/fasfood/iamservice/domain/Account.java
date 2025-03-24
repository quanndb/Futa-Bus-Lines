package com.fasfood.iamservice.domain;

import com.fasfood.common.domain.AuditableDomain;
import com.fasfood.common.enums.AccountStatus;
import com.fasfood.common.enums.Gender;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdateAccountCmd;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Account extends AuditableDomain {
    private UUID id;
    private Boolean deleted;
    private String password;
    private String email;
    private String fullName;
    private String avatarUrl;
    private Gender gender;
    private AccountStatus status;
    private String phoneNumber;
    private Set<AccountRole> roles;

    // domain logic
    public Account(CreateOrUpdateAccountCmd cmd) {
        this.id = IdUtils.nextId();
        // encoded password
        this.password = cmd.getPassword();
        this.email = cmd.getEmail();
        this.fullName = cmd.getFullName();
        this.gender = cmd.getGender();
        this.status = AccountStatus.ACTIVE;
        this.phoneNumber = cmd.getPhoneNumber();
        this.deleted = Boolean.FALSE;
        this.roles = this.createRoles(cmd.getRoleIds());
    }

    public void updateAccount(CreateOrUpdateAccountCmd cmd) {
        this.fullName = cmd.getFullName();
        this.gender = cmd.getGender();
        this.phoneNumber = cmd.getPhoneNumber();
        this.updateRole(cmd.getRoleIds());
    }

    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    private void updateRole(Set<UUID> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        this.roles.forEach(role -> {
            if (!roleIds.contains(role.getId())) {
                role.delete();
            }
            roleIds.remove(role.getId());
        });
        this.roles.addAll(this.createRoles(roleIds));
    }

    public void delete() {
        this.deleted = Boolean.TRUE;
    }

    // utils
    private Set<AccountRole> createRoles(Set<UUID> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new HashSet<>();
        }
        return roleIds.stream()
                .map((roleId) -> new AccountRole(this.id, roleId)).collect(Collectors.toSet());
    }

    public void enrichAccountRoles(Set<AccountRole> roles) {
        this.roles = roles;
    }
}
