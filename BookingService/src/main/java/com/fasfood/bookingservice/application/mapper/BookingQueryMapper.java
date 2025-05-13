package com.fasfood.bookingservice.application.mapper;

import com.fasfood.bookingservice.application.dto.request.BookingPagingRequest;
import com.fasfood.bookingservice.domain.query.BookingPagingQuery;
import com.fasfood.common.mapper.QueryMapper;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BookingQueryMapper extends QueryMapper<BookingPagingQuery, BookingPagingRequest> {
    default BookingPagingQuery from(UUID userId, BookingPagingRequest request) {
        if (userId == null || request == null) {
            return null;
        }
        BookingPagingQuery query = this.queryFromRequest(request);
        query.setUserId(userId);
        return query;
    }
}
