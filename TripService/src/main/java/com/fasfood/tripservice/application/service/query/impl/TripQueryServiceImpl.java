package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.client.client.booking.BookingClient;
import com.fasfood.client.client.iam.IamClient;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.dto.response.SeatDTO;
import com.fasfood.common.dto.response.StatisticResponse;
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
import com.fasfood.tripservice.application.dto.response.TripDetailsTransitDTO;
import com.fasfood.tripservice.application.dto.response.TripResponse;
import com.fasfood.tripservice.application.dto.response.TripTransitDTO;
import com.fasfood.tripservice.application.service.query.TripQueryService;
import com.fasfood.tripservice.domain.Trip;
import com.fasfood.tripservice.domain.query.TripPagingQuery;
import com.fasfood.tripservice.domain.repository.BusTypeRepository;
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
import com.fasfood.tripservice.infrastructure.persistence.repository.projection.TripStatisticProjection;
import com.fasfood.tripservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import com.fasfood.web.support.AbstractQueryService;
import com.fasfood.web.support.DomainRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final BusTypeRepository busTypeRepository;
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
                                   BusTypeDTOMapper busTypeDTOMapper, SeatEntityRepository seatEntityRepository, BookingClient bookingClient, IamClient iamClient, BusTypeRepository busTypeRepository) {
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
        this.busTypeRepository = busTypeRepository;
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
    public TripDTO getById(UUID id) {
        return this.enrichTransitPoints(super.getById(id));
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

    @Override
    public TripDetailsTransitDTO getTripDetailsTransit(UUID id, LocalDate departureDate) {
        TripDetailsEntity details = this.tripDetailsEntityRepository.findByIdAndDepartureDate(id, departureDate)
                .orElseThrow(() -> new ResponseException(NotFoundError.TRIP_DETAILS_NOT_FOUND));
        List<TripTransitDTO> tripTransitDTOS = this.tripTransitDTOMapper
                .entityToDTO(this.tripTransitEntityRepository.findAllByTripId(details.getTripId()));
        tripTransitDTOS.sort(Comparator.comparingInt(TripTransitDTO::getTransitOrder));
        Map<UUID, TransitPointDTO> transitPointDTOMap = this.transitPointDTOMapper
                .entityToDTO(this.transitPointEntityRepository
                        .findAllById(tripTransitDTOS.stream().map(TripTransitDTO::getTransitPointId).toList()))
                .stream().collect(Collectors.toMap(TransitPointDTO::getId, Function.identity()));
        tripTransitDTOS.forEach(tripTransit -> {
            if (transitPointDTOMap.containsKey(tripTransit.getTransitPointId())) {
                tripTransit.setTransitPoint(transitPointDTOMap.get(tripTransit.getTransitPointId()));
            }
        });
        Map<BusTypeEnum, BusTypeDTO> typeMap = this.busTypeDTOMapper
                .domainToDTO(this.busTypeRepository.findAll())
                .stream().collect(Collectors.toMap(BusTypeDTO::getType, Function.identity()));
        List<SeatDTO> firstFloorSeat = new ArrayList<>();
        List<SeatDTO> secondFloorSeat = new ArrayList<>();
        if (typeMap.containsKey(details.getType())) {
            firstFloorSeat.addAll(typeMap.get(details.getType()).getFirstFloorSeats());
            secondFloorSeat.addAll(typeMap.get(details.getType()).getSecondFloorSeats());
        }
        return TripDetailsTransitDTO.builder()
                .tripDetailsId(details.getId())
                .departureDate(departureDate)
                .pricePerSeat(details.getPrice())
                .firstFloorSeats(firstFloorSeat)
                .secondFloorSeats(secondFloorSeat)
                .type(details.getType())
                .departure(tripTransitDTOS.getFirst().getTransitPoint().getName())
                .departureTime(tripTransitDTOS.getFirst().getArrivalTime())
                .destination(tripTransitDTOS.getLast().getTransitPoint().getName())
                .destinationTime(tripTransitDTOS.getLast().getArrivalTime())
                .transitPoints(tripTransitDTOS)
                .build();
    }

    @Override
    public List<StatisticResponse> getTripStatistics(Integer year) {
        List<TripStatisticProjection> statistics;
        List<StatisticResponse> responses = new ArrayList<>();
        if (Objects.nonNull(year)) {
            statistics = this.tripEntityRepository.getCountByMonth(year);
        } else {
            statistics = this.tripEntityRepository.getCountByYear();
        }
        for (TripStatisticProjection projection : statistics) {
            responses.add(new StatisticResponse(projection.getKey(), projection.getTotal()));
        }
        return responses;
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

    private TripDTO enrichTransitPoints(TripDTO tripDTO) {
        Map<UUID, TransitPointDTO> transitDTOS = this.transitPointDTOMapper.entityToDTO(this.transitPointEntityRepository
                        .findAllById(tripDTO.getTripTransits().stream().map(TripTransitDTO::getTransitPointId).toList()))
                .stream().collect(Collectors.toMap(TransitPointDTO::getId, Function.identity()));
        tripDTO.getTripTransits().forEach(tripTransit -> {
            if (transitDTOS.containsKey(tripTransit.getTransitPointId())) {
                tripTransit.setTransitPoint(transitDTOS.get(tripTransit.getTransitPointId()));
            }
        });
        return tripDTO;
    }

    private List<TripResponse> enrichAvailableTrip(List<TripResponse> responses, LocalDate startDate) {
        List<UUID> detailsIds = responses.stream().map(TripResponse::getId).toList();
        Map<UUID, List<String>> clientResponse = new HashMap<>();
        if (!CollectionUtils.isEmpty(detailsIds)) {
            try {
                var res = this.bookingClient
                        .getBooked(new GetBookedRequest(responses.stream().map(TripResponse::getId).toList(), startDate));
                if (res.isSuccess()) {
                    clientResponse = res.getData();
                }
            } catch (Exception ignored) {
            }
        }
        for (Iterator<TripResponse> iterator = responses.iterator(); iterator.hasNext(); ) {
            TripResponse item = iterator.next();
            item.setDepartureDate(startDate);
            if (clientResponse.containsKey(item.getId())) {
                int availableSeat = item.getDetails().getTypeDetails().getSeatCapacity()
                        - clientResponse.get(item.getId()).size();
                if (availableSeat > 0) {
                    BusTypeDTO original = item.getDetails().getTypeDetails();
                    BusTypeDTO copy = new BusTypeDTO(original);
                    copy.setSeatCapacity(availableSeat);
                    item.getDetails().setTypeDetails(copy);
                } else {
                    iterator.remove();
                }
            }
        }
        return responses;
    }
}
