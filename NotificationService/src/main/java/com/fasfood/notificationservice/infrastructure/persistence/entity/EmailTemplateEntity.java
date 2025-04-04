package com.fasfood.notificationservice.infrastructure.persistence.entity;

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
@Table(name = "email_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "name", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, nullable = false)
    private String name;
    @Column(name = "description", length = ValidateConstraint.LENGTH.CONTENT_MAX_LENGTH, nullable = false)
    private String description;
    @Column(name = "code", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, nullable = false)
    private String code;
    @Column(name = "body", nullable = false)
    private String body;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
