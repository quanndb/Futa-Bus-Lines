package com.fasfood.bookingservice.infrastructure.domainrepository;

import com.fasfood.bookingservice.domain.Booking;
import com.fasfood.bookingservice.domain.BookingWay;
import com.fasfood.bookingservice.domain.Ticket;
import com.fasfood.bookingservice.domain.repository.BookingRepository;
import com.fasfood.bookingservice.infrastructure.persistence.mapper.BookingEntityMapper;
import com.fasfood.bookingservice.infrastructure.persistence.mapper.TicketEntityMapper;
import com.fasfood.bookingservice.infrastructure.persistence.repository.BookingEntityRepository;
import com.fasfood.bookingservice.infrastructure.persistence.repository.TicketEntityRepository;
import com.fasfood.bookingservice.infrastructure.support.enums.BookingType;
import com.fasfood.bookingservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.common.exception.ResponseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingEntityRepository bookingEntityRepository;
    private final BookingEntityMapper bookingEntityMapper;
    private final TicketEntityRepository ticketEntityRepository;
    private final TicketEntityMapper ticketEntityMapper;

    @Override
    public Optional<Booking> findById(String id) {
        return Optional.of(this.findAllByIds(new ArrayList<>(List.of(id))).getFirst());
    }

    @Override
    public List<Booking> findAllByIds(List<String> ids) {
        List<Booking> bookings = new ArrayList<>();
        List<BookingWay> bookingWays = this.bookingEntityMapper
                .toDomain(this.bookingEntityRepository.findAllByCode(ids));
        bookingWays.forEach(bookingWay -> ids.remove(bookingWay.getCode()));
        if (!CollectionUtils.isEmpty(ids)) {
            throw new ResponseException(NotFoundError.NOT_FOUND_BOOKING, ids);
        }
        Map<UUID, List<Ticket>> tickets = this.ticketEntityMapper.toDomain(this.ticketEntityRepository.findAllByBookingIds(
                bookingWays.stream().map(BookingWay::getId).collect(Collectors.toList())
        )).stream().collect(Collectors.groupingBy(Ticket::getBookingId));
        bookingWays.forEach(item -> {
            if (tickets.containsKey(item.getId())) {
                item.enrichTickets(tickets.get(item.getId()));
            }
        });
        Map<String, List<BookingWay>> bookingMap = bookingWays.stream().collect(Collectors.groupingBy(BookingWay::getCode));
        bookingMap.values().forEach(entities -> {
            if (entities.size() == 1) {
                bookings.add(new Booking().enrich(entities.getFirst(), null));
            } else {
                if (BookingType.DEPARTURE.equals(entities.getFirst().getType())) {
                    bookings.add(new Booking().enrich(entities.getFirst(), entities.getLast()));
                } else {
                    bookings.add(new Booking().enrich(entities.getLast(), entities.getFirst()));
                }
            }
        });
        return bookings;
    }

    @Override
    @Transactional
    public Booking save(Booking domain) {
        return this.saveAll(List.of(domain)).getFirst();
    }

    @Override
    @Transactional
    public List<Booking> saveAll(List<Booking> domains) {
        List<BookingWay> bookingWays = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();
        domains.forEach(domain -> {
            bookingWays.add(BookingWay.makePoor(domain.getDepartureTrip()));
            tickets.addAll(domain.getDepartureTrip().getTickets());
            if (Objects.nonNull(domain.getReturnTrip())) {
                bookingWays.add(BookingWay.makePoor(domain.getReturnTrip()));
                tickets.addAll(domain.getReturnTrip().getTickets());
            }
        });
        this.bookingEntityRepository.saveAll(this.bookingEntityMapper.toEntity(bookingWays));
        this.ticketEntityRepository.saveAll(this.ticketEntityMapper.toEntity(tickets));
        return domains;
    }

    @Override
    public Booking getById(String id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.NOT_FOUND_BOOKING));
    }
}
