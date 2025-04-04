package com.fasfood.notificationservice.domain.repository;

import com.fasfood.notificationservice.domain.EmailTemplate;
import com.fasfood.web.support.DomainRepository;

import java.util.UUID;

public interface EmailTemplateRepository extends DomainRepository<EmailTemplate, UUID> {
    EmailTemplate findByCode(String code);
}
