package com.fasfood.bookingservice.infrastructure.persistence.entity;

import com.fasfood.bookingservice.infrastructure.support.enums.BookingStatus;
import com.fasfood.bookingservice.infrastructure.support.enums.BookingType;
import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.common.validator.ValidateConstraint;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID id;
    @Column(name = "user_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID userId;
    // booking
    @Column(name = "code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String code;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private BookingStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private BookingType type;
    @Column(name = "payment_link")
    private String paymentLink;
    @Enumerated(EnumType.STRING)
    @Column(name = "bus_type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private BusTypeEnum busType;
    // user
    @Column(name = "full_name", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String fullName;
    @Column(name = "email", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private String email;
    @Column(name = "phone", length = ValidateConstraint.LENGTH.PHONE_MAX_LENGTH, nullable = false)
    private String phone;
    // trip
    @Column(name = "trip_details_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID tripDetailsId;
    @Column(name = "route", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, nullable = false)
    private String route;
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;
    @Column(name = "departure_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID departureId;
    @Column(name = "departure", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, nullable = false)
    private String departure;
    @Column(name = "departure_address", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, nullable = false)
    private String departureAddress;
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;
    @Column(name = "destination_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID destinationId;
    @Column(name = "destination", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, nullable = false)
    private String destination;
    @Column(name = "destination_address", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, nullable = false)
    private String destinationAddress;
    @Column(name = "destination_time", nullable = false)
    private LocalTime destinationTime;
    // ticket
    @Column(name = "price_per_seat", nullable = false)
    private Long pricePerSeat;
    @Column(name = "num_of_tickets", nullable = false)
    private int numOfTickets;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
