package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.tripservice.infrastructure.persistence.entity.BusTypeEntity;
import com.fasfood.common.enums.BusTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusTypeEntityRepository extends JpaRepository<BusTypeEntity, UUID> {
    @Override
    @Query("SELECT a FROM BusTypeEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<BusTypeEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM BusTypeEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<BusTypeEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM BusTypeEntity a WHERE a.type = :type AND a.type != :except AND a.deleted = false")
    Optional<BusTypeEntity> findByCodeExcept(BusTypeEnum type, BusTypeEnum except);

    @Query("SELECT a FROM BusTypeEntity a WHERE a.type = :type AND a.deleted = false")
    Optional<BusTypeEntity> findByCode(BusTypeEnum type);

    @Override
    @Query("SELECT a FROM BusTypeEntity a WHERE a.deleted = false")
    List<BusTypeEntity> findAll();
}
