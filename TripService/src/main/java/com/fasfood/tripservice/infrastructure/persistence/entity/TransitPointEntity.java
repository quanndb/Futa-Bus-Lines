package com.fasfood.tripservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.validator.ValidateConstraint;
import com.fasfood.tripservice.infrastructure.support.enums.TransitType;
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
@Table(name = "transit_point")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransitPointEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "name", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, nullable = false)
    private String name;
    @Column(name = "address", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, nullable = false)
    private String address;
    @Column(name = "hotline", length = ValidateConstraint.LENGTH.PHONE_MAX_LENGTH)
    private String hotline;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private TransitType type;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
