package com.fasfood.notificationservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.notificationservice.application.dto.response.EmailTemplateDTO;
import com.fasfood.notificationservice.domain.EmailTemplate;
import com.fasfood.notificationservice.infrastructure.persistence.entity.EmailTemplateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailTemplateDTOMapper extends DTOMapper<EmailTemplateDTO, EmailTemplate, EmailTemplateEntity> {
}
