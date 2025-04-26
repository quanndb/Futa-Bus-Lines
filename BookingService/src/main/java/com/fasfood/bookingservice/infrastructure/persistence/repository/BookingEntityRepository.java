package com.fasfood.bookingservice.infrastructure.persistence.repository;

import com.fasfood.bookingservice.infrastructure.persistence.entity.BookingEntity;
import com.fasfood.bookingservice.infrastructure.persistence.repository.custom.CustomBookingEntityRepository;
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookingProjection;
import com.fasfood.persistence.custom.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingEntityRepository extends EntityRepository<BookingEntity, UUID>, CustomBookingEntityRepository {
    @Query("SELECT a FROM BookingEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<BookingEntity> findAllByIds(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM BookingEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<BookingEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM BookingEntity a WHERE a.code IN :codes AND a.deleted = false ORDER BY a.lastModifiedAt")
    List<BookingEntity> findAllByCode(Iterable<String> codes);

    @Query("SELECT t.seatNumber FROM BookingEntity a LEFT JOIN TicketEntity t ON t.bookingId = a.id " +
            "WHERE a.tripDetailsId = :tripDetailsId AND a.departureDate = :departureDate " +
            "AND a.deleted = false AND t.deleted = false AND a.status = 'PAYED'")
    List<String> getBookedSeats(UUID tripDetailsId, LocalDate departureDate);

    @Query("SELECT a.tripDetailsId AS tripDetailsId, t.seatNumber AS seatNumber " +
            "FROM BookingEntity a " +
            "LEFT JOIN TicketEntity t ON t.bookingId = a.id " +
            "WHERE a.tripDetailsId IN :tripDetailsId " +
            "AND a.departureDate = :departureDate " +
            "AND a.deleted = false AND t.deleted = false AND a.status = 'PAYED'")
    List<BookingProjection> getBookedSeats(List<UUID> tripDetailsId, LocalDate departureDate);
}
