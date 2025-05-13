package com.fasfood.tripservice.application.service.cmd.impl;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.commonexcel.CellValidator;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.tripservice.application.dto.mapper.TripDTOMapper;
import com.fasfood.tripservice.application.dto.mapper.TripDetailsDTOMapper;
import com.fasfood.tripservice.application.dto.request.TransitPointCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripCreator;
import com.fasfood.tripservice.application.dto.request.TripDetailsCreateRequest;
import com.fasfood.tripservice.application.dto.request.TripDetailsUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripTransitCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripTransitListCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.TripDTO;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import com.fasfood.tripservice.application.mapper.TripCommandMapper;
import com.fasfood.tripservice.application.service.cmd.TransitPointCommandService;
import com.fasfood.tripservice.application.service.cmd.TripCommandService;
import com.fasfood.tripservice.domain.TransitPoint;
import com.fasfood.tripservice.domain.Trip;
import com.fasfood.tripservice.domain.TripDetails;
import com.fasfood.tripservice.domain.cmd.TripCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.cmd.TripDetailsCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.cmd.TripTransitCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.repository.TripRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.TransitPointEntity;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import com.fasfood.tripservice.infrastructure.persistence.mapper.TripEntityMapper;
import com.fasfood.tripservice.infrastructure.persistence.repository.TransitPointEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripDetailsEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripEntityRepository;
import com.fasfood.tripservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripCommandServiceImpl implements TripCommandService {

    private final TripRepository tripRepository;
    private final TripEntityRepository tripEntityRepository;
    private final TripDetailsEntityRepository tripDetailsEntityRepository;
    private final TransitPointCommandService transitPointCommandService;
    private final TransitPointEntityRepository transitPointEntityRepository;
    private final TripCommandMapper tripCommandMapper;
    private final TripDTOMapper tripDTOMapper;
    private final TripEntityMapper tripEntityMapper;
    private final TripDetailsDTOMapper tripDetailsDTOMapper;

    @Override
    @Transactional
    public TripDTO create(TripCreateOrUpdateRequest request) {
        this.tripChecker(request, null);
        TripCreateOrUpdateCmd cmd = this.tripCommandMapper.cmdFromRequest(request);
        return this.tripDTOMapper.domainToDTO(this.tripRepository.save(new Trip(cmd)));
    }

    @Override
    @Transactional
    public TripDTO update(UUID id, TripCreateOrUpdateRequest request) {
        Trip found = this.tripRepository.getById(id);
        this.tripChecker(request, found.getCode());
        TripCreateOrUpdateCmd cmd = this.tripCommandMapper.cmdFromRequest(request);
        return this.tripDTOMapper.domainToDTO(this.tripRepository.save(found.update(cmd)));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Trip found = this.tripRepository.getById(id);
        found.delete();
        this.tripRepository.save(found);
    }

    @Override
    @Transactional
    public TripDTO setTripTransits(UUID id, TripTransitListCreateOrUpdateRequest request) {
        Trip found = this.tripRepository.getById(id);
        this.checkTransitPoints(request);
        List<TripTransitCreateOrUpdateCmd> cmd = this.tripCommandMapper.fromTransits(request.getTransits());
        return this.tripDTOMapper.domainToDTO(this.tripRepository.save(found.setTripTransit(cmd)));
    }

    @Override
    @Transactional
    public void upload(MultipartFile file) {
        List<TripCreator> tripCreators = this.extractTripCreator(file);
        Map<String, UUID> transitPoints = this.transitPointCommandService
                .saveAll(this.extractTransitFromTripCreator(tripCreators))
                .stream().collect(Collectors.toMap(TransitPoint::getName, TransitPoint::getId));
        List<TripCreateOrUpdateRequest> requests = this.extractTripFromTripCreator(tripCreators, transitPoints);
        List<TripCreateOrUpdateCmd> cmd = this.tripCommandMapper.cmdListFromRequestList(requests);
        List<Trip> trips = new ArrayList<>();
        cmd.forEach(command -> trips.add(new Trip(command)));
        this.tripEntityRepository.deleteAll();
        this.tripRepository.saveAll(trips);
    }

    @Override
    public void upLoadTripDetails(MultipartFile file) {
        List<TripDetailsCreateOrUpdateCmd> cmds = this.tripCommandMapper.fromDetails(this.extractTripDetails(file));
        Set<String> tripCodes = cmds.stream().map(TripDetailsCreateOrUpdateCmd::getTripCode).collect(Collectors.toSet());
        Map<String, Trip> domains = this.tripEntityRepository.findAllByCode(tripCodes)
                .stream().collect(Collectors.toMap(TripEntity::getCode, item -> {
                    Trip current = this.tripEntityMapper.toDomain(item);
                    current.enrichTripTransits(List.of());
                    current.enrichTripDetails(List.of());
                    return current;
                }));
        domains.values().forEach(domain -> tripCodes.remove(domain.getCode()));
        if (!CollectionUtils.isEmpty(tripCodes)) {
            throw new ResponseException(BadRequestError.NOT_EXISTED_TRIP_CODE, tripCodes.toString());
        }
        cmds.forEach(command -> {
            if (domains.containsKey(command.getTripCode())) {
                domains.get(command.getTripCode()).createTripDetail(command);
            }
        });
        this.tripDetailsEntityRepository.deleteAll();
        this.tripRepository.saveAll(domains.values().stream().toList());
    }

    @Override
    public TripDetailsDTO createDetails(UUID id, TripDetailsCreateRequest request) {
        Trip found = this.tripRepository.getById(id);
        TripDetailsCreateOrUpdateCmd cmd = this.tripCommandMapper.from(request);
        TripDetails newOne = found.createTripDetail(cmd);
        this.tripRepository.save(found);
        return this.tripDetailsDTOMapper.domainToDTO(newOne);
    }

    @Override
    public TripDetailsDTO updateDetails(UUID id, UUID detailsId, TripDetailsUpdateRequest request) {
        Trip found = this.tripRepository.getById(id);
        TripDetailsCreateOrUpdateCmd cmd = this.tripCommandMapper.from(request);
        TripDetails newOne = found.updateTripDetail(detailsId, cmd);
        this.tripRepository.save(found);
        return this.tripDetailsDTOMapper.domainToDTO(newOne);
    }

    @Override
    public void deleteDetails(UUID id, UUID detailsId) {
        Trip found = this.tripRepository.getById(id);
        found.deleteTripDetail(detailsId);
        this.tripRepository.save(found);
    }

    private void tripChecker(TripCreateOrUpdateRequest request, String except) {
        this.tripEntityRepository.findByCodeExcept(request.getCode(), except)
                .ifPresent(tripEntity -> {
                    throw new ResponseException(BadRequestError.EXISTED_TRIP_CODE, request.getCode());
                });
    }

    private void checkTransitPoints(TripTransitListCreateOrUpdateRequest request) {
        Set<UUID> transitPointIds = request.getTransits().stream()
                .map(TripTransitCreateOrUpdateRequest::getTransitPointId).collect(Collectors.toSet());
        List<TransitPointEntity> founds = this.transitPointEntityRepository.findAllById(transitPointIds);
        founds.forEach(transitPointEntity -> transitPointIds.remove(transitPointEntity.getId()));
        if (!CollectionUtils.isEmpty(transitPointIds)) {
            throw new ResponseException(BadRequestError.NOT_EXISTED_TRANSIT_POINT, transitPointIds);
        }
    }

    private List<TripCreator> extractTripCreator(MultipartFile file) {
        // Create a mapper for the User class
        var mapper = ExcelExtractor.extractTrip();
        // Add validation rules
        mapper.column("Code").addValidationRule(CellValidator.notNull());
        mapper.column("Name").addValidationRule(CellValidator.notNull());
        mapper.column("Address").addValidationRule(CellValidator.notNull());
        mapper.column("Transit type (PLACE/STATION/OFFICE/TRANSPORT)").addValidationRule(CellValidator.notNull());
        mapper.column("Arrival time (HH:mm)").addValidationRule(CellValidator.notNull());
        mapper.column("Trip transit type (PICKUP/DROP/BOTH)").addValidationRule(CellValidator.notNull());

        // Import from file
        ExcelUtil.ImportResult<TripCreator> result = mapper.importFromFile(file, "Trips", true);

        // Check for validation errors
        if (result.hasErrors()) {
            throw new ResponseException(BadRequestError.INVALID_FORMAT, result.getErrors());
        }

        return result.getValidData();
    }

    private List<TripDetailsCreateRequest> extractTripDetails(MultipartFile file) {
        // Create a mapper for the User class
        var mapper = ExcelExtractor.extractTripDetails();
        // Add validation rules
        mapper.column("Trip code").addValidationRule(CellValidator.notNull());
        mapper.column("From date (yy-mm-dd)").addValidationRule(CellValidator.notNull());
        mapper.column("To date (yy-mm-dd)").addValidationRule(CellValidator.notNull());
        mapper.column("Type (SEAT/BED/LIMOUSINE)").addValidationRule(CellValidator.notNull());
        mapper.column("Price").addValidationRule(CellValidator.notNull());
        mapper.column("Status (ACTIVE/INACTIVE)").addValidationRule(CellValidator.notNull());

        // Import from file
        ExcelUtil.ImportResult<TripDetailsCreateRequest> result = mapper.importFromFile(file, "Trip details", true);

        // Check for validation errors
        if (result.hasErrors()) {
            throw new ResponseException(BadRequestError.INVALID_FORMAT, result.getErrors());
        }

        return result.getValidData();
    }

    private Set<TransitPointCreateOrUpdateRequest> extractTransitFromTripCreator(List<TripCreator> tripCreators) {
        if (CollectionUtils.isEmpty(tripCreators)) return new HashSet<>();
        Set<TransitPointCreateOrUpdateRequest> requests = new HashSet<>();
        tripCreators.forEach(tripCreator -> requests.add(TransitPointCreateOrUpdateRequest.builder()
                .name(tripCreator.getName())
                .address(tripCreator.getAddress())
                .type(tripCreator.getTransitType())
                .hotline(tripCreator.getHotline())
                .build()));
        return requests;
    }

    private List<TripCreateOrUpdateRequest> extractTripFromTripCreator(List<TripCreator> tripCreators, Map<String, UUID> transitPoints) {
        if (CollectionUtils.isEmpty(tripCreators)) return new ArrayList<>();
        Map<String, TripCreateOrUpdateRequest> transitPointMap = new HashMap<>();
        tripCreators.forEach(tripCreator -> {
            TripCreateOrUpdateRequest tripRequest;
            if (transitPointMap.containsKey(tripCreator.getCode())) {
                tripRequest = transitPointMap.get(tripCreator.getCode());
            } else {
                tripRequest = TripCreateOrUpdateRequest.builder()
                        .code(tripCreator.getCode())
                        .transits(new ArrayList<>())
                        .build();
            }
            tripRequest.getTransits().add(TripTransitCreateOrUpdateRequest.builder()
                    .arrivalTime(tripCreator.getArrivalTime())
                    .transitPointId(transitPoints.get(tripCreator.getName()))
                    .type(tripCreator.getTripTransitType())
                    .build());
            transitPointMap.put(tripCreator.getCode(), tripRequest);
        });
        return new ArrayList<>(transitPointMap.values());
    }
}
