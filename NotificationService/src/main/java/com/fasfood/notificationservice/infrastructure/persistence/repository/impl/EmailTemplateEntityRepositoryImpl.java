package com.fasfood.notificationservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.notificationservice.domain.query.EmailTemplatePagingQuery;
import com.fasfood.notificationservice.infrastructure.persistence.entity.EmailTemplateEntity;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.util.QueryBuilder;

import java.util.List;

public class EmailTemplateEntityRepositoryImpl extends AbstractPagingEntityRepository<EmailTemplateEntity, EmailTemplatePagingQuery, StatisticResponse> {

    protected EmailTemplateEntityRepositoryImpl() {
        super(EmailTemplateEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, EmailTemplatePagingQuery query) {
        queryBuilder.like(List.of("id", "name", "description", "code"), query.getKeyword())
                .whereIn("id", query.getIds())
                .where("deleted", false);
    }
}
