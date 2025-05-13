package com.fasfood.bookingservice.application.service.query.impl;

import com.fasfood.bookingservice.application.dto.mapper.BookingDTOMapper;
import com.fasfood.bookingservice.application.dto.request.BookingPagingRequest;
import com.fasfood.bookingservice.application.dto.response.BookingDTO;
import com.fasfood.bookingservice.application.mapper.BookingQueryMapper;
import com.fasfood.bookingservice.application.service.query.BookingQueryService;
import com.fasfood.bookingservice.domain.Booking;
import com.fasfood.bookingservice.domain.query.BookingPagingQuery;
import com.fasfood.bookingservice.domain.repository.BookingRepository;
import com.fasfood.bookingservice.infrastructure.persistence.repository.BookingEntityRepository;
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookedProjection;
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookingStatisticProjection;
import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.web.support.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingQueryServiceImpl implements BookingQueryService {

    private final BookingRepository bookingRepository;
    private final BookingEntityRepository bookingEntityRepository;
    private final BookingDTOMapper bookingDTOMapper;
    private final BookingQueryMapper queryMapper;

    @Override
    public BookingDTO findByCode(String code) {
        return this.bookingDTOMapper.from(this.bookingRepository.getById(code));
    }

    @Override
    public PageDTO<BookingDTO> findBooking(BookingPagingRequest request) {
        return this.findBooking(this.queryMapper.queryFromRequest(request));
    }

    @Override
    public PageDTO<BookingDTO> findMyBooking(BookingPagingRequest request) {
        return this.findBooking(this.queryMapper
                .from(SecurityUtils.getUserId(), request));
    }

    @Override
    public Map<UUID, List<String>> getBooked(GetBookedRequest request) {
        List<BookedProjection> booked = this.bookingEntityRepository.getBooked(request);
        if (CollectionUtils.isEmpty(booked)) {
            return new HashMap<>();
        }
        return booked.stream().collect(Collectors.groupingBy(
                BookedProjection::getTripDetailsId,
                Collectors.mapping(BookedProjection::getSeatNumber, Collectors.toList())
        ));
    }

    @Override
    public List<String> getBooked(UUID detailsId, LocalDate departureDate) {
        return this.getBooked(new GetBookedRequest(List.of(detailsId), departureDate)).get(detailsId);
    }

    @Override
    public List<StatisticResponse> getStatistics(Integer year) {
        List<BookingStatisticProjection> statistics;
        List<StatisticResponse> responses = new ArrayList<>();
        if (Objects.nonNull(year)) {
            statistics = this.bookingEntityRepository.getCountByMonth(year);
        } else {
            statistics = this.bookingEntityRepository.getCountByYear();
        }
        for (BookingStatisticProjection projection : statistics) {
            responses.add(new StatisticResponse(projection.getKey(), projection.getTotal()));
        }
        return responses;
    }

    @Override
    public List<StatisticResponse> getRevenueStatistics(Integer year) {
        List<BookingStatisticProjection> statistics;
        List<StatisticResponse> responses = new ArrayList<>();
        if (Objects.nonNull(year)) {
            statistics = this.bookingEntityRepository.getRevenueByMonth(year);
        } else {
            statistics = this.bookingEntityRepository.getRevenueByYear();
        }
        for (BookingStatisticProjection projection : statistics) {
            responses.add(new StatisticResponse(projection.getKey(), projection.getTotal()));
        }
        return responses;
    }

    private PageDTO<BookingDTO> findBooking(BookingPagingQuery query) {
        long count = this.bookingEntityRepository.count(query);
        if (count == 0) return PageDTO.empty();
        List<String> codes = this.bookingEntityRepository.searchCode(query);
        List<Booking> data = this.bookingRepository.findAllByIds(codes);
        return PageDTO.of(this.bookingDTOMapper.from(data), query.getPageIndex(), query.getPageSize(), count);
    }
}
