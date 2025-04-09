package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.PlaceEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaceEntityRepository extends EntityRepository<PlaceEntity, UUID> {
    @Override
    @Query("SELECT a FROM PlaceEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<PlaceEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Query("SELECT a FROM PlaceEntity a WHERE a.code IN :codes AND a.deleted = false")
    List<PlaceEntity> findAllByCodes(@Param("codes") Iterable<String> codes);

    @Override
    @Query("SELECT a FROM PlaceEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<PlaceEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM PlaceEntity a WHERE a.code = :code AND a.deleted = false")
    Optional<PlaceEntity> findByCode(@Param("code") String code);

    @Query("SELECT a FROM PlaceEntity a WHERE a.code = :code AND a.code != :except AND a.deleted = false")
    Optional<PlaceEntity> findByCodeExcept(@Param("code") String code, @Param("except") String except);

    @Modifying
    @Transactional
    @Query("UPDATE PlaceEntity a SET a.deleted = true")
    void deleteAll();
}
