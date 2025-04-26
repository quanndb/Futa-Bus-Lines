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
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookingProjection;
import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.error.AuthenticationError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.web.support.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                .from(SecurityUtils.getCurrentUser()
                        .orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED)), request));
    }

    @Override
    public Map<UUID, List<String>> getBooked(GetBookedRequest request) {
        List<BookingProjection> booked = this.bookingEntityRepository.getBookedSeats(request.getDetailsIds(), request.getStartDate());
        if (CollectionUtils.isEmpty(booked)) {
            return new HashMap<>();
        }
        return booked.stream().collect(Collectors.groupingBy(
                BookingProjection::getTripDetailsId,
                Collectors.mapping(BookingProjection::getSeatNumber, Collectors.toList())
        ));
    }

    private PageDTO<BookingDTO> findBooking(BookingPagingQuery query) {
        long count = this.bookingEntityRepository.count(query);
        if (count == 0) return PageDTO.empty();
        List<String> codes = this.bookingEntityRepository.searchCode(query);
        List<Booking> data = this.bookingRepository.findAllByIds(codes);
        data.forEach(entity -> {});
        return PageDTO.of(this.bookingDTOMapper.from(data), query.getPageIndex(), query.getPageSize(), count);
    }
}
