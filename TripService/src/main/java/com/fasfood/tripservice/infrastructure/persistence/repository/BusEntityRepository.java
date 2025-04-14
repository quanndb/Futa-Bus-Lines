package com.fasfood.tripservice.infrastructure.persistence.repository;

import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusEntityRepository extends EntityRepository<BusEntity, UUID> {
    @Override
    @Query("SELECT a FROM BusEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<BusEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Query("SELECT a FROM BusEntity a WHERE a.licensePlate IN :licensePlates AND a.deleted = false")
    List<BusEntity> findAllByLicensePlate(@Param("licensePlates") Iterable<String> licensePlates);

    @Override
    @Query("SELECT a FROM BusEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<BusEntity> findById(@Param("id") UUID id);

    boolean existsByLicensePlate(String licensePlate);

    @Modifying
    @Transactional
    @Query("UPDATE BusEntity a SET a.deleted = true")
    void deleteAll();
}
