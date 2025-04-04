package com.fasfood.notificationservice.application.mapper;

import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.notificationservice.application.dto.request.EmailTemplatePagingRequest;
import com.fasfood.notificationservice.domain.query.EmailTemplatePagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationQueryMapper extends QueryMapper<EmailTemplatePagingQuery, EmailTemplatePagingRequest> {
}
