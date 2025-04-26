package com.fasfood.bookingservice.infrastructure.persistence.mapper;

import com.fasfood.bookingservice.domain.Ticket;
import com.fasfood.bookingservice.infrastructure.persistence.entity.TicketEntity;
import com.fasfood.common.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketEntityMapper extends EntityMapper<Ticket, TicketEntity> {
}
