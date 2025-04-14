package com.fasfood.tripservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.validator.ValidateConstraint;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
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

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "trip_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripDetailsEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "from_at", nullable = false)
    private Instant fromAt;
    @Column(name = "to_at", nullable = false)
    private Instant toAt;
    @Column(name = "driver_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID driverId;
    @Column(name = "assistant_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID assistantId;
    @Column(name = "trip_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID tripId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private BusTypeEnum type;
    @Column(name = "bus_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID busId;
    @Column(name = "price", nullable = false)
    private long price;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private TripStatus status;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
