package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.application.dto.mapper.PlaceDTOMapper;
import com.fasfood.tripservice.application.dto.request.RoutePagingRequest;
import com.fasfood.tripservice.application.dto.response.PlaceDTO;
import com.fasfood.tripservice.application.dto.response.RouteDTO;
import com.fasfood.tripservice.application.service.query.RouteQueryService;
import com.fasfood.tripservice.domain.Route;
import com.fasfood.tripservice.domain.query.RoutePagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.RouteEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.PlaceEntityRepository;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import com.fasfood.web.support.AbstractQueryService;
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

    private final PlaceEntityRepository placeEntityRepository;
    private final PlaceDTOMapper placeDTOMapper;

    protected RouteQueryServiceImpl(EntityRepository<RouteEntity, UUID> entityRepository,
                                    DTOMapper<RouteDTO, Route, RouteEntity> dtoMapper,
                                    QueryMapper<RoutePagingQuery, RoutePagingRequest> pagingRequestMapper,
                                    PlaceEntityRepository placeEntityRepository, PlaceDTOMapper placeDTOMapper) {
        super(entityRepository, dtoMapper, pagingRequestMapper);
        this.placeEntityRepository = placeEntityRepository;
        this.placeDTOMapper = placeDTOMapper;
    }

    @Override
    public byte[] getTemplate() {
        return ExcelExtractor.extractRoute().exportToBytes("Routes", List.of());
    }

    @Override
    public List<RouteDTO> enrichRouteDTO(List<RouteDTO> routeDTOList) {
        Set<String> placeCodeSet = new HashSet<>();
        routeDTOList.forEach(routeDTO -> {
            placeCodeSet.add(routeDTO.getDepartureCode());
            placeCodeSet.add(routeDTO.getDestinationCode());
        });
        Map<String, PlaceDTO> placeDTOS = this.placeDTOMapper
                .entityToDTO(this.placeEntityRepository.findAllByCodes(placeCodeSet))
                .stream()
                .collect(Collectors.toMap(PlaceDTO::getCode, Function.identity()));
        routeDTOList.forEach(routeDTO -> {
            if (placeDTOS.containsKey(routeDTO.getDepartureCode())) {
                routeDTO.setDeparture(placeDTOS.get(routeDTO.getDepartureCode()));
            }
            if (placeDTOS.containsKey(routeDTO.getDestinationCode())) {
                routeDTO.setDestination(placeDTOS.get(routeDTO.getDestinationCode()));
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
