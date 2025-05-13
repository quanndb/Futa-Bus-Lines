package com.fasfood.bookingservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingStatisticRequest extends Request {
    @Min(value = 2020, message = "INVALID_YEAR")
    private Integer year;
}
