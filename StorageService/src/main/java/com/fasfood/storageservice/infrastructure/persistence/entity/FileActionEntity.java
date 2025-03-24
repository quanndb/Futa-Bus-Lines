package com.fasfood.storageservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.validator.ValidateConstraint;
import com.fasfood.storageservice.infrastructure.support.enums.FileActionEnum;
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
@Table(name = "file_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileActionEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "action", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private FileActionEnum action;
    @Column(name = "file_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID fileId;
    @Column(name = "sender_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID senderId;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
