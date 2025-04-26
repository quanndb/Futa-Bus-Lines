package com.fasfood.bookingservice.application.mapper;

import com.fasfood.bookingservice.application.dto.request.BookingRequest;
import com.fasfood.bookingservice.domain.cmd.BookingCreateCmd;
import com.fasfood.bookingservice.domain.cmd.BookingWayCreateCmd;
import com.fasfood.common.dto.response.TripDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface BookingCmdMapper {

    BookingCreateCmd from(BookingRequest booking);

    @Mapping(target = "seats", ignore = true)
    BookingWayCreateCmd from(TripDetailsResponse bookingWay);

    default BookingCreateCmd from(TripDetailsResponse departure, TripDetailsResponse destination, BookingRequest request) {
        BookingCreateCmd cmd = this.from(request);
        var departureMapped = this.from(departure);
        var destinationMapped = this.from(destination);
        departureMapped.setSeats(cmd.getDepartureTrip().getSeats());
        if(Objects.nonNull(cmd.getReturnTrip())){
            destinationMapped.setSeats(cmd.getReturnTrip().getSeats());
        }
        cmd.setDepartureTrip(departureMapped);
        cmd.setReturnTrip(destinationMapped);
        return cmd;
    }
}
