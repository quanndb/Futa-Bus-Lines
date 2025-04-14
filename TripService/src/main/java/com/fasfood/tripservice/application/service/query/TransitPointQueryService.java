package com.fasfood.tripservice.application.service.query;

import com.fasfood.common.query.QueryService;
import com.fasfood.tripservice.application.dto.request.TransitPointPagingRequest;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;

import java.util.UUID;

public interface TransitPointQueryService extends QueryService<TransitPointDTO, UUID, TransitPointPagingRequest> {
    byte[] getTemplate();
}
