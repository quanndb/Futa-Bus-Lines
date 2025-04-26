package com.fasfood.bookingservice.application.mapper;

import com.fasfood.bookingservice.application.dto.request.BookingPagingRequest;
import com.fasfood.bookingservice.domain.query.BookingPagingQuery;
import com.fasfood.common.mapper.QueryMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingQueryMapper extends QueryMapper<BookingPagingQuery, BookingPagingRequest> {
    default BookingPagingQuery from(String sender, BookingPagingRequest request) {
        if (sender == null || sender.isEmpty() || request == null) {
            return null;
        }
        BookingPagingQuery query = this.queryFromRequest(request);
        query.setSender(sender);
        return query;
    }
}
