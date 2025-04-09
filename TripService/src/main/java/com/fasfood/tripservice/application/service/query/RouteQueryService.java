package com.fasfood.tripservice.application.service.query;

import com.fasfood.common.query.QueryService;
import com.fasfood.tripservice.application.dto.request.RoutePagingRequest;
import com.fasfood.tripservice.application.dto.response.RouteDTO;

import java.util.List;
import java.util.UUID;

public interface RouteQueryService extends QueryService<RouteDTO, UUID, RoutePagingRequest> {
    byte[] getTemplate();

    List<RouteDTO> enrichRouteDTO(List<RouteDTO> routeDTOList);

    default RouteDTO enrichRoute(RouteDTO routeDTO) {
        return this.enrichRouteDTO(List.of(routeDTO)).getFirst();
    }
}
