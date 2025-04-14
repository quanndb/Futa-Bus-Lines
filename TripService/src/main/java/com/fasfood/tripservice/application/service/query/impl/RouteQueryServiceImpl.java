package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.application.dto.mapper.TransitPointDTOMapper;
import com.fasfood.tripservice.application.dto.request.RoutePagingRequest;
import com.fasfood.tripservice.application.dto.response.RouteDTO;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
import com.fasfood.tripservice.application.service.query.RouteQueryService;
import com.fasfood.tripservice.domain.Route;
import com.fasfood.tripservice.domain.query.RoutePagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.RouteEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.TransitPointEntityRepository;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import com.fasfood.web.support.AbstractQueryService;
import com.fasfood.web.support.DomainRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RouteQueryServiceImpl extends AbstractQueryService<Route, RouteEntity, RouteDTO, UUID, RoutePagingQuery, RoutePagingRequest>
        implements RouteQueryService {

    private final TransitPointEntityRepository placeEntityRepository;
    private final TransitPointDTOMapper placeDTOMapper;

    protected RouteQueryServiceImpl(DomainRepository<Route, UUID> domainRepository,
                                    EntityRepository<RouteEntity, UUID> entityRepository,
                                    DTOMapper<RouteDTO, Route, RouteEntity> dtoMapper,
                                    QueryMapper<RoutePagingQuery, RoutePagingRequest> pagingRequestMapper,
                                    TransitPointEntityRepository placeEntityRepository,
                                    TransitPointDTOMapper placeDTOMapper) {
        super(domainRepository, entityRepository, dtoMapper, pagingRequestMapper);
        this.placeEntityRepository = placeEntityRepository;
        this.placeDTOMapper = placeDTOMapper;
    }


    @Override
    public byte[] getTemplate() {
        return ExcelExtractor.extractRoute().exportToBytes("Routes", List.of());
    }

    @Override
    public List<RouteDTO> enrichRouteDTO(List<RouteDTO> routeDTOList) {
        Set<UUID> placeIds = new HashSet<>();
        routeDTOList.forEach(routeDTO -> {
            placeIds.add(routeDTO.getDepartureId());
            placeIds.add(routeDTO.getDestinationId());
        });
        Map<UUID, TransitPointDTO> placeDTOS = this.placeDTOMapper
                .entityToDTO(this.placeEntityRepository.findAllById(placeIds))
                .stream()
                .collect(Collectors.toMap(TransitPointDTO::getId, Function.identity()));
        routeDTOList.forEach(routeDTO -> {
            if (placeDTOS.containsKey(routeDTO.getDepartureId())) {
                routeDTO.setDeparture(placeDTOS.get(routeDTO.getDepartureId()).getName());
            }
            if (placeDTOS.containsKey(routeDTO.getDestinationId())) {
                routeDTO.setDestination(placeDTOS.get(routeDTO.getDestinationId()).getName());
            }
        });
        return routeDTOList;
    }

    @Override
    public PageDTO<RouteDTO> getList(RoutePagingRequest pagingRequest) {
        PageDTO<RouteDTO> routeDTOPageDTO = super.getList(pagingRequest);
        routeDTOPageDTO.setData(enrichRouteDTO(routeDTOPageDTO.getData()));
        return routeDTOPageDTO;
    }
}
