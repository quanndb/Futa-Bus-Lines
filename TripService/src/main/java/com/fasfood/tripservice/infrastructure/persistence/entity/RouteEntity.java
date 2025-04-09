package com.fasfood.tripservice.infrastructure.persistence.entity;

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
@Table(name = "route")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "departure_code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String departureCode;
    @Column(name = "destination_code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String destinationCode;
    @Column(name = "distance", nullable = false)
    private Double distance;
    @Column(name = "duration", nullable = false)
    private Integer duration;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
