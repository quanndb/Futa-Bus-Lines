package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.RouteEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RouteEntityRepository extends EntityRepository<RouteEntity, UUID> {
    @Override
    @Query("SELECT a FROM RouteEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<RouteEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM RouteEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<RouteEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM RouteEntity a WHERE a.departureId = :departureId " +
            "AND a.destinationId = :destinationId AND a.deleted = false")
    Optional<RouteEntity> findByDepartureIdAndDepartureId(@Param("departureId") UUID departureId,
                                                          @Param("destinationId") UUID destinationId);

    @Query("SELECT a FROM RouteEntity a WHERE a.departureId = :departureId AND a.destinationId = :destinationId " +
            "AND a.departureId != :exceptDepartureId AND a.destinationId != :exceptDestinationId " +
            "AND a.deleted = false")
    Optional<RouteEntity> findByDepartureIdAndDepartureId(@Param("departureId") UUID departureId,
                                                          @Param("destinationId") UUID destinationId,
                                                          @Param("exceptDepartureId") UUID exceptDepartureId,
                                                          @Param("exceptDestinationId") UUID exceptDestinationId);

    @Modifying
    @Transactional
    @Query("UPDATE RouteEntity a SET a.deleted = true")
    void deleteAll();
}
