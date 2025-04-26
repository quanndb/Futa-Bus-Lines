package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.tripservice.infrastructure.persistence.entity.TripTransitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripTransitEntityRepository extends JpaRepository<TripTransitEntity, UUID> {
    @Override
    @Query("SELECT a FROM TripTransitEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<TripTransitEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM TripTransitEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<TripTransitEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM TripTransitEntity a WHERE a.tripId = :tripId AND a.deleted = false")
    List<TripTransitEntity> findAllByTripId(UUID tripId);

    @Query("SELECT a FROM TripTransitEntity a WHERE a.tripId IN :tripIds AND a.deleted = false order by a.transitOrder")
    List<TripTransitEntity> findAllByTripIds(Iterable<UUID> tripIds);

    @Query("SELECT a FROM TripTransitEntity a WHERE a.tripId = :tripId AND a.transitPointId IN :transitIds AND a.deleted = false order by a.transitOrder")
    List<TripTransitEntity> findAllByTripIdAndTransitIds(UUID tripId, Iterable<UUID> transitIds);
}
