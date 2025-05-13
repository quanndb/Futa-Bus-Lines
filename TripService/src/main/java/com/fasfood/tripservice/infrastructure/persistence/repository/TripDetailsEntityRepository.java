package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.tripservice.infrastructure.persistence.entity.TripDetailsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripDetailsEntityRepository extends JpaRepository<TripDetailsEntity, UUID> {
    @Override
    @Query("SELECT a FROM TripDetailsEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<TripDetailsEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM TripDetailsEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<TripDetailsEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM TripDetailsEntity a WHERE a.tripId = :tripId AND a.deleted = false")
    List<TripDetailsEntity> findAllByTripId(@Param("tripId") UUID tripId);

    @Query("SELECT a FROM TripDetailsEntity a WHERE a.tripId = :ids AND a.deleted = false")
    List<TripDetailsEntity> findAllByTripIds(@Param("ids") Iterable<UUID> ids);

    @Query("SELECT a FROM TripDetailsEntity a WHERE a.id = :detailsId AND :departureDate BETWEEN DATE(a.fromDate) AND DATE(a.toDate) AND a.deleted = false")
    Optional<TripDetailsEntity> findByIdAndDepartureDate(UUID detailsId, LocalDate departureDate);

    @Modifying
    @Transactional
    @Query("UPDATE TripDetailsEntity a SET a.deleted = true")
    void deleteAll();
}
