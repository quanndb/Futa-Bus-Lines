package com.fasfood.bookingservice.application.dto.response;

import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookedProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookedResponse {
    List<BookedProjection> bookings;
}
