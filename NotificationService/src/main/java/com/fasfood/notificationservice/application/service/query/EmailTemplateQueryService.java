package com.fasfood.notificationservice.application.service.query;

import com.fasfood.common.query.QueryService;
import com.fasfood.notificationservice.application.dto.request.EmailTemplatePagingRequest;
import com.fasfood.notificationservice.application.dto.response.EmailTemplateDTO;

import java.util.UUID;

public interface EmailTemplateQueryService extends QueryService<EmailTemplateDTO, UUID, EmailTemplatePagingRequest> {
}