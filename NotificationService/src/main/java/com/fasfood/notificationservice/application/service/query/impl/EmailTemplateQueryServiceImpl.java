package com.fasfood.notificationservice.application.service.query.impl;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.notificationservice.application.dto.request.EmailTemplatePagingRequest;
import com.fasfood.notificationservice.application.dto.response.EmailTemplateDTO;
import com.fasfood.notificationservice.application.service.query.EmailTemplateQueryService;
import com.fasfood.notificationservice.domain.EmailTemplate;
import com.fasfood.notificationservice.domain.query.EmailTemplatePagingQuery;
import com.fasfood.notificationservice.infrastructure.persistence.entity.EmailTemplateEntity;
import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.web.support.AbstractQueryService;
import com.fasfood.web.support.DomainRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Getter
@Setter
@Service
public class EmailTemplateQueryServiceImpl extends AbstractQueryService<EmailTemplate, EmailTemplateEntity, EmailTemplateDTO, UUID, EmailTemplatePagingQuery, EmailTemplatePagingRequest>
        implements EmailTemplateQueryService {

    protected EmailTemplateQueryServiceImpl(DomainRepository<EmailTemplate, UUID> domainRepository,
                                            EntityRepository<EmailTemplateEntity, UUID> entityRepository,
                                            DTOMapper<EmailTemplateDTO, EmailTemplate, EmailTemplateEntity> dtoMapper,
                                            QueryMapper<EmailTemplatePagingQuery, EmailTemplatePagingRequest> pagingRequestMapper) {
        super(domainRepository, entityRepository, dtoMapper, pagingRequestMapper);
    }
}
