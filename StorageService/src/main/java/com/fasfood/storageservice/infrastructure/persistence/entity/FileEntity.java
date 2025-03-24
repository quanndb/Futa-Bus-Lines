package com.fasfood.storageservice.infrastructure.persistence.entity;

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
@Table(name = "file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "owner_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID ownerId;
    @Column(name = "name", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, nullable = false)
    private String name;
    @Column(name = "path", length = ValidateConstraint.LENGTH.CONTENT_MAX_LENGTH, nullable = false)
    private String path;
    @Column(name = "type", length = ValidateConstraint.LENGTH.VALUE_MAX_LENGTH, nullable = false)
    private String type;
    @Column(name = "extension", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String extension;
    @Column(name = "sharing", nullable = false)
    private Boolean sharing;
    @Column(name = "size", nullable = false)
    private Long size;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
