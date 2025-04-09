package com.fasfood.tripservice.application.service.cmd.impl;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.commonexcel.CellValidator;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.tripservice.application.dto.mapper.RouteDTOMapper;
import com.fasfood.tripservice.application.dto.request.RouteCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.RouteDTO;
import com.fasfood.tripservice.application.mapper.RouteCommandMapper;
import com.fasfood.tripservice.application.service.cmd.RouteCommandService;
import com.fasfood.tripservice.domain.Route;
import com.fasfood.tripservice.domain.cmd.RouteCreateOrUpDateCmd;
import com.fasfood.tripservice.domain.repository.RouteRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.PlaceEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.PlaceEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.RouteEntityRepository;
import com.fasfood.tripservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteCommandServiceImpl implements RouteCommandService {

    private final PlaceEntityRepository placeEntityRepository;
    private final RouteEntityRepository routeEntityRepository;
    private final RouteCommandMapper commandMapper;
    private final RouteDTOMapper routeDTOMapper;
    private final RouteRepository routeRepository;

    @Override
    @Transactional
    public void upload(MultipartFile file) {
        List<RouteCreateOrUpdateRequest> requests = this.extractPlaceRequestFromFile(file);
        Set<String> placeCodes = new HashSet<>();
        requests.forEach(request -> {
            placeCodes.add(request.getDepartureCode());
            placeCodes.add(request.getDestinationCode());
        });
        this.checkExistPlaces(placeCodes);
        List<RouteCreateOrUpDateCmd> cmds = this.commandMapper.cmdListFromRequestList(requests);
        List<Route> routes = new ArrayList<>();
        cmds.forEach(cmd -> {
            routes.add(new Route(cmd));
        });
        this.routeEntityRepository.deleteAll();
        this.routeRepository.saveAll(routes);
    }

    @Override
    @Transactional
    public RouteDTO create(RouteCreateOrUpdateRequest request) {
        this.checkExistPlaces(Set.of(request.getDepartureCode(), request.getDestinationCode()));
        this.checkExistedRoute(request.getDepartureCode(), request.getDestinationCode());
        RouteCreateOrUpDateCmd cmd = this.commandMapper.cmdFromRequest(request);
        return this.routeDTOMapper.domainToDTO(this.routeRepository.save(new Route(cmd)));
    }

    @Override
    @Transactional
    public RouteDTO update(UUID id, RouteCreateOrUpdateRequest request) {
        Route route = this.routeRepository.getById(id);
        this.checkExistPlaces(Set.of(request.getDepartureCode(), request.getDestinationCode()));
        this.checkExistedRouteExcept(request.getDepartureCode(), request.getDestinationCode(), route.getDepartureCode(), route.getDestinationCode());
        RouteCreateOrUpDateCmd cmd = this.commandMapper.cmdFromRequest(request);
        return this.routeDTOMapper.domainToDTO(this.routeRepository.save(route.update(cmd)));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Route route = this.routeRepository.getById(id);
        route.delete();
        this.routeRepository.save(route);
    }

    private void checkExistPlaces(Set<String> places) {
        if (CollectionUtils.isEmpty(places)) {
            return;
        }
        List<String> placesCopy = new ArrayList<>(places);
        List<PlaceEntity> placeEntities = this.placeEntityRepository.findAllByCodes(places);
        placeEntities.forEach(placeEntity -> {
            placesCopy.remove(placeEntity.getCode());
        });
        if (!CollectionUtils.isEmpty(placesCopy)) {
            throw new ResponseException(BadRequestError.NOT_EXISTED_CODE, placesCopy);
        }
    }

    private List<RouteCreateOrUpdateRequest> extractPlaceRequestFromFile(MultipartFile file) {
        // Create a mapper for the User class
        var mapper = ExcelExtractor.extractRoute();

        // Add validation rules
        mapper.column("Departure Code").addValidationRule(CellValidator.notNull());
        mapper.column("Destination Code").addValidationRule(CellValidator.notNull());
        mapper.column("Distance (km)").addValidationRule(CellValidator.notNull());
        mapper.column("Duration (min)").addValidationRule(CellValidator.notNull());

        // Import from file
        ExcelUtil.ImportResult<RouteCreateOrUpdateRequest> result = mapper.importFromFile(file, "Routes", true);

        // Check for validation errors
        if (result.hasErrors()) {
            throw new ResponseException(BadRequestError.INVALID_FORMAT, result.getErrors());
        }

        return this.getRouteCreateOrUpdateRequests(result);
    }

    private List<RouteCreateOrUpdateRequest> getRouteCreateOrUpdateRequests(ExcelUtil.ImportResult<RouteCreateOrUpdateRequest> result) {
        List<RouteCreateOrUpdateRequest> routes = result.getValidData();
        Set<String> seenRoutes = new HashSet<>();
        List<String> errors = new ArrayList<>();
        for (RouteCreateOrUpdateRequest route : routes) {
            String departureCode = route.getDepartureCode();
            String destinationCode = route.getDestinationCode();

            String key = departureCode + "->" + destinationCode;
            if (seenRoutes.contains(key)) {
                errors.add(key);
            } else {
                seenRoutes.add(key);
            }
        }
        if(!CollectionUtils.isEmpty(errors)) {
            throw new ResponseException(BadRequestError.EXISTED_CODE, errors);
        }
        return routes;
    }

    private void checkExistedRoute(String departureCode, String destinationCode) {
        List<String> errors = new ArrayList<>();
        this.routeEntityRepository.findByDepartureCodeAndDepartureCode(departureCode, destinationCode)
                .ifPresent(routeEntity
                        -> {
                    errors.add(routeEntity.getDepartureCode() + "->" + routeEntity.getDestinationCode());
                });
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ResponseException(BadRequestError.EXISTED_CODE, errors);
        }
    }

    private void checkExistedRouteExcept(String departureCode, String destinationCode, String exceptDepartureCode, String exceptDestinationCode) {
        List<String> errors = new ArrayList<>();
        this.routeEntityRepository.findByDepartureCodeAndDepartureCode(departureCode, destinationCode, exceptDepartureCode, exceptDestinationCode)
                .ifPresent(routeEntity
                        -> {
                    errors.add(routeEntity.getDepartureCode() + "->" + routeEntity.getDestinationCode());
                });
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ResponseException(BadRequestError.EXISTED_CODE, errors);
        }
    }
}
