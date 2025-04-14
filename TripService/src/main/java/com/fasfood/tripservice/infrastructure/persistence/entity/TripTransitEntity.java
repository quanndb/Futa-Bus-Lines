package com.fasfood.tripservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.validator.ValidateConstraint;
import com.fasfood.tripservice.infrastructure.support.enums.TripTransitType;
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

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "trip_transit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripTransitEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "trip_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID tripId;
    @Column(name = "transit_point_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID transitPointId;
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;
    @Column(name = "transit_order", nullable = false)
    private int transitOrder;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private TripTransitType type;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
