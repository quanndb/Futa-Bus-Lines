package com.fasfood.tripservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.validator.ValidateConstraint;
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
@Table(name = "bus_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusTypeEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private BusTypeEnum type;
    @Column(name = "seat_capacity", nullable = false)
    private int seatCapacity;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
