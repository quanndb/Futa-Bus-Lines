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

    @Query("SELECT a FROM RouteEntity a WHERE a.departureCode = :departureCode " +
            "AND a.destinationCode = :destinationCode AND a.deleted = false")
    Optional<RouteEntity> findByDepartureCodeAndDepartureCode(@Param("departureCode") String departureCode,
                                                              @Param("destinationCode") String destinationCode);

    @Query("SELECT a FROM RouteEntity a WHERE a.departureCode = :departureCode AND a.destinationCode = :destinationCode " +
            "AND a.departureCode != :exceptDepartureCode AND a.destinationCode != :exceptDestinationCode " +
            "AND a.deleted = false")
    Optional<RouteEntity> findByDepartureCodeAndDepartureCode(@Param("departureCode") String departureCode,
                                                              @Param("destinationCode") String destinationCode,
                                                              @Param("exceptDepartureCode") String exceptDepartureCode,
                                                              @Param("exceptDestinationCode") String exceptDestinationCode);

    @Modifying
    @Transactional
    @Query("UPDATE RouteEntity a SET a.deleted = true")
    void deleteAll();
}
