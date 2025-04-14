package com.fasfood.tripservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.validator.ValidateConstraint;
import com.fasfood.tripservice.infrastructure.support.enums.BusStatus;
import com.fasfood.common.enums.BusTypeEnum;
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
@Table(name = "bus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "license_plate", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String licensePlate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private BusStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private BusTypeEnum type;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
