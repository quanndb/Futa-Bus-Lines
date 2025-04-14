package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.tripservice.infrastructure.persistence.entity.SeatEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatEntityRepository extends JpaRepository<SeatEntity, UUID> {
    @Override
    @Query("SELECT a FROM SeatEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<SeatEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Query("SELECT a FROM SeatEntity a WHERE a.typeId = :busId AND a.deleted = false")
    List<SeatEntity> findAllByTypeId(@Param("busId") UUID busId);

    @Query("SELECT a FROM SeatEntity a WHERE a.typeId IN :busIds AND a.deleted = false")
    List<SeatEntity> findAllByTypeIds(@Param("busIds") Iterable<UUID> busIds);

    @Override
    @Query("SELECT a FROM SeatEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<SeatEntity> findById(@Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE SeatEntity a SET a.deleted = true")
    void deleteAll();
}
