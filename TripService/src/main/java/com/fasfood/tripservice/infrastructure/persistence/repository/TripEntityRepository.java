package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.custom.CustomTripEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.projection.TripStatisticProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripEntityRepository extends EntityRepository<TripEntity, UUID>, CustomTripEntityRepository {
    @Override
    @Query("SELECT a FROM TripEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<TripEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM TripEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<TripEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM TripEntity a WHERE a.code IN :codes AND a.deleted = false")
    List<TripEntity> findAllByCode(Iterable<String> codes);

    @Query("SELECT a FROM TripEntity a WHERE a.code = :code AND a.code != :except AND a.deleted = false")
    Optional<TripEntity> findByCodeExcept(String code, String except);

    @Modifying
    @Transactional
    @Query("UPDATE TripEntity a SET a.deleted = true")
    void deleteAll();

    @Query(value = """
    SELECT 
        EXTRACT(YEAR FROM gs) AS key, 
        COUNT(*) AS total
    FROM trip_details b
    JOIN generate_series(b.from_date, b.to_date, interval '1 day') AS gs ON TRUE
    WHERE 
        b.status = 'ACTIVE' 
        AND b.deleted = FALSE
    GROUP BY EXTRACT(YEAR FROM gs)
    ORDER BY key ASC
""", nativeQuery = true)
    List<TripStatisticProjection> getCountByYear();

    @Query(value = """
    SELECT 
        EXTRACT(MONTH FROM gs) AS key, 
        COUNT(*) AS total
    FROM trip_details b
    JOIN generate_series(b.from_date, b.to_date, interval '1 day') AS gs ON TRUE
    WHERE 
        b.status = 'ACTIVE' 
        AND b.deleted = FALSE
        AND EXTRACT(YEAR FROM gs) = :year
    GROUP BY EXTRACT(MONTH FROM gs)
    ORDER BY key ASC
""", nativeQuery = true)
    List<TripStatisticProjection> getCountByMonth(@Param("year") int year);
}
