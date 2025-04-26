package com.fasfood.bookingservice.infrastructure.persistence.repository;

import com.fasfood.bookingservice.infrastructure.persistence.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TicketEntityRepository extends JpaRepository<TicketEntity, UUID> {

    @Query("SELECT a FROM TicketEntity a WHERE a.bookingId IN :ids AND a.deleted = false ORDER BY a.bookingId, a.seatNumber")
    List<TicketEntity> findAllByBookingIds(List<UUID> ids);
}
