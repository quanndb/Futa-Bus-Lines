package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.TransitPointEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransitPointEntityRepository extends EntityRepository<TransitPointEntity, UUID> {
    @Override
    @Query("SELECT a FROM TransitPointEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<TransitPointEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM TransitPointEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<TransitPointEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM TransitPointEntity a WHERE a.name = :name AND a.deleted = false")
    Optional<TransitPointEntity> findByName(@Param("name") String name);

    @Query("SELECT a FROM TransitPointEntity a WHERE a.name = :name AND (:except IS NULL OR a.name != :except) AND a.deleted = false")
    Optional<TransitPointEntity> findByNameExcept(@Param("name") String name, @Param("except") String except);

    @Modifying
    @Transactional
    @Query("UPDATE TransitPointEntity a SET a.deleted = true")
    void deleteAll();
}
