package com.fasfood.tripservice.application.service.query;

import com.fasfood.common.query.QueryService;
import com.fasfood.tripservice.application.dto.request.PlacePagingRequest;
import com.fasfood.tripservice.application.dto.response.PlaceDTO;

import java.util.UUID;

public interface PlaceQueryService extends QueryService<PlaceDTO, UUID, PlacePagingRequest> {
    byte[] getTemplate();
}
