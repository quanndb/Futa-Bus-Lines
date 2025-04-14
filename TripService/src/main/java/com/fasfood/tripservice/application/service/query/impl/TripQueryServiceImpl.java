package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.client.client.booking.BookingClient;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.application.dto.mapper.BusDTOMapper;
import com.fasfood.tripservice.application.dto.mapper.BusTypeDTOMapper;
import com.fasfood.tripservice.application.dto.mapper.TransitPointDTOMapper;
import com.fasfood.tripservice.application.dto.mapper.TripDetailsDTOMapper;
import com.fasfood.tripservice.application.dto.mapper.TripTransitDTOMapper;
import com.fasfood.tripservice.application.dto.request.TripFilterRequest;
import com.fasfood.tripservice.application.dto.request.TripPagingRequest;
import com.fasfood.tripservice.application.dto.response.BusDTO;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
import com.fasfood.tripservice.application.dto.response.TripDTO;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import com.fasfood.tripservice.application.dto.response.TripResponse;
import com.fasfood.tripservice.application.dto.response.TripTransitDTO;
import com.fasfood.tripservice.application.service.query.TripQueryService;
import com.fasfood.tripservice.domain.Trip;
import com.fasfood.tripservice.domain.query.TripPagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.BusEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.BusTypeEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TransitPointEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripDetailsEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripTransitEntityRepository;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import com.fasfood.web.support.AbstractQueryService;
import com.fasfood.web.support.DomainRepository;
import org.springframework.stereotype.Service;

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
    private final BusEntityRepository busEntityRepository;
    private final BusDTOMapper busDTOMapper;
    private final BusTypeEntityRepository busTypeEntityRepository;
    private final BusTypeDTOMapper busTypeDTOMapper;
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
                                   BusEntityRepository busEntityRepository,
                                   BusDTOMapper busDTOMapper, BusTypeEntityRepository busTypeEntityRepository,
                                   BusTypeDTOMapper busTypeDTOMapper, BookingClient bookingClient) {
        super(domainRepository, entityRepository, dtoMapper, pagingRequestMapper);
        this.tripDetailsDTOMapper = tripDetailsDTOMapper;
        this.tripEntityRepository = tripEntityRepository;
        this.tripDetailsEntityRepository = tripDetailsEntityRepository;
        this.tripTransitEntityRepository = tripTransitEntityRepository;
        this.tripTransitDTOMapper = tripTransitDTOMapper;
        this.transitPointEntityRepository = transitPointEntityRepository;
        this.transitPointDTOMapper = transitPointDTOMapper;
        this.busEntityRepository = busEntityRepository;
        this.busDTOMapper = busDTOMapper;
        this.busTypeEntityRepository = busTypeEntityRepository;
        this.busTypeDTOMapper = busTypeDTOMapper;
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

    private List<TripResponse> enrichTrip(List<TripDetailsDTO> detailsDTOS) {
        // bus type
        Map<BusTypeEnum, BusTypeDTO> typeMap = this.busTypeDTOMapper.entityToDTO(this.busTypeEntityRepository.findAll())
                .stream().collect(Collectors.toMap(BusTypeDTO::getType, Function.identity()));
        // bus
        Map<UUID, BusDTO> busDTOMap = this.busDTOMapper
                .entityToDTO(this.busEntityRepository
                        .findAllById(detailsDTOS.stream()
                                .map(TripDetailsDTO::getBusId).toList()))
                .stream().collect(Collectors.toMap(BusDTO::getId, Function.identity()));
        // enrich details
        detailsDTOS.forEach(detailsDTO -> {
            if (typeMap.containsKey(detailsDTO.getType())) {
                detailsDTO.setTypeDetails(typeMap.get(detailsDTO.getType()));
            }
            if (busDTOMap.containsKey(detailsDTO.getBusId())) {
                detailsDTO.setBus(busDTOMap.get(detailsDTO.getBusId()));
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
        Map<UUID, List<String>> bookedData = new HashMap<>();
//        Response<Map<UUID, List<String>>> clientResponse = this.bookingClient
//                .getBooked(new GetBookedRequest(responses.stream().map(TripResponse::getId).toList(), startDate));
//        if (clientResponse.isSuccess()) {
//            bookedData = clientResponse.getData();
//        }
        for (TripResponse item : responses) {
            if (bookedData.containsKey(item.getId())) {
                int availableSeat = item.getDetails().getTypeDetails().getSeatCapacity() - bookedData.get(item.getId()).size();
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
