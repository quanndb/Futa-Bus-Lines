package com.fasfood.notificationservice.infrastructure.persistence.repository;

import com.fasfood.notificationservice.infrastructure.persistence.entity.EmailTemplateEntity;
import com.fasfood.persistence.custom.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailTemplateEntityRepository extends EntityRepository<EmailTemplateEntity, UUID> {
    @Override
    @Query("SELECT a FROM EmailTemplateEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<EmailTemplateEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM EmailTemplateEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<EmailTemplateEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM EmailTemplateEntity a WHERE a.code = :code AND a.deleted = false")
    Optional<EmailTemplateEntity> findByCode(@Param("code") String code);
}
