package com.fasfood.bookingservice.domain.query;

import com.fasfood.bookingservice.infrastructure.support.enums.BookingStatus;
import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BookingPagingQuery extends PagingQuery {
    private UUID userId;
    private String code;
    private LocalDate departureDate;
    private BookingStatus status;
}
