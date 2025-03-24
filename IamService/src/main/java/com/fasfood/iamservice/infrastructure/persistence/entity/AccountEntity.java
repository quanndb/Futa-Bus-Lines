package com.fasfood.iamservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.enums.AccountStatus;
import com.fasfood.common.enums.Gender;
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
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Column(name = "phoneNumber")
    private String phoneNumber;
}
