package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.client.client.booking.BookingClient;
import com.fasfood.client.client.iam.IamClient;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.dto.response.TripDetailsResponse;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.application.dto.mapper.BusTypeDTOMapper;
import com.fasfood.tripservice.application.dto.mapper.TransitPointDTOMapper;
import com.fasfood.tripservice.application.dto.mapper.TripDetailsDTOMapper;
import com.fasfood.tripservice.application.dto.mapper.TripTransitDTOMapper;
import com.fasfood.tripservice.application.dto.request.TripFilterRequest;
import com.fasfood.tripservice.application.dto.request.TripPagingRequest;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
import com.fasfood.tripservice.application.dto.response.TripDTO;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import com.fasfood.tripservice.application.dto.response.TripResponse;
import com.fasfood.tripservice.application.dto.response.TripTransitDTO;
import com.fasfood.tripservice.application.service.query.TripQueryService;
import com.fasfood.tripservice.domain.Trip;
import com.fasfood.tripservice.domain.query.TripPagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.TransitPointEntity;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripDetailsEntity;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripTransitEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.BusTypeEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.SeatEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TransitPointEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripDetailsEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripTransitEntityRepository;
import com.fasfood.tripservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import com.fasfood.web.support.AbstractQueryService;
import com.fasfood.web.support.DomainRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TripQueryServiceImpl
        extends AbstractQueryService<Trip, TripEntity, TripDTO, UUID, TripPagingQuery, TripPagingRequest>
        implements TripQueryService {

    private final TripDetailsDTOMapper tripDetailsDTOMapper;
    private final TripEntityRepository tripEntityRepository;
    private final TripDetailsEntityRepository tripDetailsEntityRepository;
    private final TripTransitEntityRepository tripTransitEntityRepository;
    private final TripTransitDTOMapper tripTransitDTOMapper;
    private final TransitPointEntityRepository transitPointEntityRepository;
    private final TransitPointDTOMapper transitPointDTOMapper;
    private final BusTypeEntityRepository busTypeEntityRepository;
    private final BusTypeDTOMapper busTypeDTOMapper;
    private final SeatEntityRepository seatEntityRepository;
    private final BookingClient bookingClient;

    protected TripQueryServiceImpl(DomainRepository<Trip, UUID> domainRepository,
                                   EntityRepository<TripEntity, UUID> entityRepository,
                                   DTOMapper<TripDTO, Trip, TripEntity> dtoMapper,
                                   QueryMapper<TripPagingQuery, TripPagingRequest> pagingRequestMapper,
                                   TripDetailsDTOMapper tripDetailsDTOMapper, TripEntityRepository tripEntityRepository,
                                   TripDetailsEntityRepository tripDetailsEntityRepository,
                                   TripTransitEntityRepository tripTransitEntityRepository,
                                   TripTransitDTOMapper tripTransitDTOMapper,
                                   TransitPointEntityRepository transitPointEntityRepository,
                                   TransitPointDTOMapper transitPointDTOMapper,
                                   BusTypeEntityRepository busTypeEntityRepository,
                                   BusTypeDTOMapper busTypeDTOMapper, SeatEntityRepository seatEntityRepository, BookingClient bookingClient, IamClient iamClient) {
        super(domainRepository, entityRepository, dtoMapper, pagingRequestMapper);
        this.tripDetailsDTOMapper = tripDetailsDTOMapper;
        this.tripEntityRepository = tripEntityRepository;
        this.tripDetailsEntityRepository = tripDetailsEntityRepository;
        this.tripTransitEntityRepository = tripTransitEntityRepository;
        this.tripTransitDTOMapper = tripTransitDTOMapper;
        this.transitPointEntityRepository = transitPointEntityRepository;
        this.transitPointDTOMapper = transitPointDTOMapper;
        this.busTypeEntityRepository = busTypeEntityRepository;
        this.busTypeDTOMapper = busTypeDTOMapper;
        this.seatEntityRepository = seatEntityRepository;
        this.bookingClient = bookingClient;
    }

    @Override
    public byte[] getTemplate() {
        return ExcelExtractor.extractTrip().exportToBytes("Trips", List.of());
    }

    @Override
    public byte[] getTripDetailsTemplate() {
        return ExcelExtractor.extractTripDetails().exportToBytes("Trip details", List.of());
    }

    @Override
    public List<TripDetailsDTO> tripDetails(UUID id) {
        Trip found = this.domainRepository.getById(id);
        return this.tripDetailsDTOMapper.domainToDTO(found.getTripDetails());
    }

    @Override
    public List<TripResponse> findTrips(TripFilterRequest tripFilterRequest) {
        List<UUID> details = this.tripEntityRepository.findTrips(tripFilterRequest);
        List<TripDetailsDTO> entities = this.tripDetailsDTOMapper
                .entityToDTO(this.tripDetailsEntityRepository.findAllById(details));
        return this.enrichAvailableTrip(this.enrichTrip(entities), tripFilterRequest.getDepartureDate());
    }

    @Override
    public TripDetailsResponse getTripDetails(UUID id, UUID departureId, UUID arrivalId, LocalDate departureDate) {
        List<UUID> detailsIds = this.tripEntityRepository.findTrips(TripFilterRequest.builder()
                .departureId(departureId)
                .destinationId(arrivalId)
                .departureDate(departureDate)
                .detailsIds(List.of(id))
                .build());
        if (CollectionUtils.isEmpty(detailsIds)) {
            throw new ResponseException(NotFoundError.TRIP_DETAILS_NOT_FOUND);
        }
        TripDetailsEntity found = this.tripDetailsEntityRepository
                .findById(detailsIds.getFirst()).orElseThrow(() -> new ResponseException(NotFoundError.TRIP_DETAILS_NOT_FOUND));
        TripEntity foundTrip = this.tripEntityRepository.findById(found.getTripId())
                .orElseThrow(() -> new ResponseException(NotFoundError.TRIP_NOT_FOUND, found.getTripId()));
        List<String> seats = this.seatEntityRepository.findALlSeatsByTypeCode(found.getType());
        List<TripTransitEntity> transits = this.tripTransitEntityRepository
                .findAllByTripIdAndTransitIds(foundTrip.getId(), List.of(departureId, arrivalId));
        List<TransitPointEntity> transitsPoint = this.transitPointEntityRepository
                .findAllById(List.of(departureId, arrivalId));
        if (CollectionUtils.isEmpty(transits) || transits.size() != 2) {
            throw new ResponseException(NotFoundError.TRIP_TRANSIT_NOT_FOUND, departureId + "->" + arrivalId);
        }
        if (CollectionUtils.isEmpty(transitsPoint) || transitsPoint.size() != 2) {
            throw new ResponseException(NotFoundError.TRANSIT_POINT_NOT_FOUND, departureId + "->" + arrivalId);
        }
        TripTransitEntity firstTripTransit = null;
        TripTransitEntity lastTripTransit = null;
        if (transits.getFirst().getTransitPointId().equals(departureId)) {
            firstTripTransit = transits.getFirst();
            lastTripTransit = transits.getLast();
        } else {
            firstTripTransit = transits.getLast();
            lastTripTransit = transits.getFirst();
        }
        TransitPointEntity firstTransitPoint = null;
        TransitPointEntity lastTransitPoint = null;
        if (transitsPoint.getFirst().getId().equals(departureId)) {
            firstTransitPoint = transitsPoint.getFirst();
            lastTransitPoint = transitsPoint.getLast();
        } else {
            firstTransitPoint = transitsPoint.getLast();
            lastTransitPoint = transitsPoint.getFirst();
        }
        return TripDetailsResponse.builder()
                .route(foundTrip.getCode())
                .type(found.getType())
                .seats(seats)
                .tripDetailsId(id)
                .departureDate(departureDate)
                .departureTime(firstTripTransit.getArrivalTime())
                .departureId(departureId)
                .departure(firstTransitPoint.getName())
                .departureAddress(firstTransitPoint.getAddress())
                .destinationId(arrivalId)
                .destinationTime(lastTripTransit.getArrivalTime())
                .destination(lastTransitPoint.getName())
                .destinationAddress(lastTransitPoint.getAddress())
                .pricePerSeat(found.getPrice())
                .build();
    }

    private List<TripResponse> enrichTrip(List<TripDetailsDTO> detailsDTOS) {
        // bus type
        Map<BusTypeEnum, BusTypeDTO> typeMap = this.busTypeDTOMapper.entityToDTO(this.busTypeEntityRepository.findAll())
                .stream().collect(Collectors.toMap(BusTypeDTO::getType, Function.identity()));
        // enrich details
        detailsDTOS.forEach(detailsDTO -> {
            if (typeMap.containsKey(detailsDTO.getType())) {
                detailsDTO.setTypeDetails(typeMap.get(detailsDTO.getType()));
            }
        });
        // trip
        Map<UUID, TripResponse> result = new HashMap<>();
        for (TripDetailsDTO dto : detailsDTOS) {
            result.put(dto.getId(), TripResponse.builder().id(dto.getId()).details(dto).tripId(dto.getTripId()).build());
        }
        // transit point
        Set<TripTransitDTO> tripTransitDTOS = new HashSet<>(this.tripTransitDTOMapper
                .entityToDTO(this.tripTransitEntityRepository.findAllByTripIds(result.values().stream().map(TripResponse::getTripId).toList())));
        Map<UUID, TransitPointDTO> transitDTOS = this.transitPointDTOMapper
                .entityToDTO(this.transitPointEntityRepository.findAllById(tripTransitDTOS.stream().map(TripTransitDTO::getTransitPointId).toList()))
                .stream().collect(Collectors.toMap(TransitPointDTO::getId, Function.identity()));
        tripTransitDTOS.forEach(tripTransitDTO -> {
            if (transitDTOS.containsKey(tripTransitDTO.getTransitPointId())) {
                tripTransitDTO.setTransitPoint(transitDTOS.get(tripTransitDTO.getTransitPointId()));
            }
        });
        Map<UUID, List<TripTransitDTO>> tripTransitDTOMap = tripTransitDTOS.stream().collect(Collectors.groupingBy(TripTransitDTO::getTripId));
        tripTransitDTOMap.forEach((tripId, transitList) ->
                transitList.sort(Comparator.comparing(TripTransitDTO::getTransitOrder))
        );
        // enrich
        result.values().forEach(item -> {
            if (tripTransitDTOMap.containsKey(item.getTripId())) {
                item.setTripTransits(tripTransitDTOMap.get(item.getTripId()));
            }
        });
        return result.values().stream().toList();
    }

    private List<TripResponse> enrichAvailableTrip(List<TripResponse> responses, LocalDate startDate) {
        List<UUID> detailsIds = responses.stream().map(TripResponse::getId).toList();
        Map<UUID, List<String>> clientResponse = new HashMap<>();
        if (!CollectionUtils.isEmpty(detailsIds)) {
            clientResponse = this.bookingClient
                    .getBooked(new GetBookedRequest(responses.stream().map(TripResponse::getId).toList(), startDate)).getData();
        }
        for (TripResponse item : responses) {
            item.setDepartureDate(startDate);
            if (clientResponse.containsKey(item.getId())) {
                int availableSeat = item.getDetails().getTypeDetails().getSeatCapacity() - clientResponse.get(item.getId()).size();
                if (availableSeat > 0) {
                    item.getDetails().getTypeDetails().setSeatCapacity(availableSeat);
                } else {
                    responses.remove(item);
                }
            }
        }
        return responses;
    }
}
