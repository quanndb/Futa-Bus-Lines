package com.fasfood.notificationservice.infrastructure.domainrepository;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.notificationservice.domain.EmailTemplate;
import com.fasfood.notificationservice.domain.repository.EmailTemplateRepository;
import com.fasfood.notificationservice.infrastructure.persistence.entity.EmailTemplateEntity;
import com.fasfood.notificationservice.infrastructure.persistence.repository.EmailTemplateEntityRepository;
import com.fasfood.notificationservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class EmailTemplateRepositoryImpl extends AbstractDomainRepository<EmailTemplate, EmailTemplateEntity, UUID>
        implements EmailTemplateRepository {

    private final EmailTemplateEntityRepository emailTemplateEntityRepository;

    protected EmailTemplateRepositoryImpl(JpaRepository<EmailTemplateEntity, UUID> jpaRepository,
                                          EntityMapper<EmailTemplate, EmailTemplateEntity> mapper,
                                          EmailTemplateEntityRepository emailTemplateEntityRepository) {
        super(jpaRepository, mapper);
        this.emailTemplateEntityRepository = emailTemplateEntityRepository;
    }

    @Override
    public EmailTemplate getById(UUID id) {
        EmailTemplateEntity entity = this.emailTemplateEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.TEMPLATE_NOTFOUND, id.toString()));
        return this.mapper.toDomain(entity);
    }

    @Override
    public EmailTemplate findByCode(String code) {
        EmailTemplateEntity entity = this.emailTemplateEntityRepository.findByCode(code)
                .orElseThrow(() -> new ResponseException(NotFoundError.TEMPLATE_NOTFOUND, code));
        return this.mapper.toDomain(entity);
    }
}
