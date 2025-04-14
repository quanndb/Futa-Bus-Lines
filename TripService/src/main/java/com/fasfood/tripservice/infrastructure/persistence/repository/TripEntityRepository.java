package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.custom.CustomTripEntityRepository;
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
}
