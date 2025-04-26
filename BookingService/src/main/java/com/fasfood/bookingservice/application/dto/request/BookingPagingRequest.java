package com.fasfood.bookingservice.application.dto.request;

import com.fasfood.bookingservice.infrastructure.support.enums.BookingStatus;
import com.fasfood.common.dto.request.PagingRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingPagingRequest extends PagingRequest {
    private String code;
    private LocalDate departureDate;
    private BookingStatus status;
}
