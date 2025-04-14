package com.fasfood.tripservice.application.service.query;

import com.fasfood.common.query.QueryService;
import com.fasfood.tripservice.application.dto.request.BusPagingRequest;
import com.fasfood.tripservice.application.dto.response.BusDTO;

import java.util.UUID;

public interface BusQueryService extends QueryService<BusDTO, UUID, BusPagingRequest> {
    byte[] getTemplate();
}
