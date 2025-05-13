package com.fasfood.tripservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.validator.ValidateConstraint;
import com.fasfood.common.enums.BusFloor;
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
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "typeId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID typeId;
    @Column(name = "seat_number", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, nullable = false)
    private String seatNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "floor", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private BusFloor floor;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    @Column(name = "seat_order")
    private Integer seatOrder;
}
