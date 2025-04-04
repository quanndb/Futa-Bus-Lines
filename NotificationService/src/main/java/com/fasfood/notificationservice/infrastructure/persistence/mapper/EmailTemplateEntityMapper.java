package com.fasfood.notificationservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.notificationservice.domain.EmailTemplate;
import com.fasfood.notificationservice.infrastructure.persistence.entity.EmailTemplateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailTemplateEntityMapper extends EntityMapper<EmailTemplate, EmailTemplateEntity> {
}
