package com.fasfood.bookingservice.infrastructure.persistence.repository;

import com.fasfood.bookingservice.infrastructure.persistence.entity.BookingEntity;
import com.fasfood.bookingservice.infrastructure.persistence.repository.custom.CustomBookingEntityRepository;
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookingStatisticProjection;
import com.fasfood.persistence.custom.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingEntityRepository extends EntityRepository<BookingEntity, UUID>, CustomBookingEntityRepository {
    @Query("SELECT a FROM BookingEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<BookingEntity> findAllByIds(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM BookingEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<BookingEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM BookingEntity a WHERE a.code IN :codes AND a.deleted = false ORDER BY a.lastModifiedAt DESC")
    List<BookingEntity> findAllByCode(Iterable<String> codes);

    @Query("""
                SELECT YEAR(b.createdAt) AS key, SUM(b.pricePerSeat * b.numOfTickets) AS total
                FROM BookingEntity b
                WHERE b.status = 'PAYED' AND b.deleted = false
                GROUP BY YEAR(b.createdAt)
                ORDER BY YEAR(b.createdAt) ASC
            """)
    List<BookingStatisticProjection> getRevenueByYear();

    @Query("""
                SELECT MONTH(b.createdAt) AS key, SUM(b.pricePerSeat * b.numOfTickets) AS total
                FROM BookingEntity b
                WHERE YEAR(b.createdAt) = :year AND b.status = 'PAYED' AND b.deleted = false
                GROUP BY MONTH(b.createdAt)
                ORDER BY MONTH(b.createdAt) ASC
            """)
    List<BookingStatisticProjection> getRevenueByMonth(int year);

    @Query("""
                SELECT YEAR(b.createdAt) AS key, COUNT(b.createdAt) AS total
                FROM BookingEntity b
                WHERE b.status = 'PAYED' AND b.deleted = false
                GROUP BY YEAR(b.createdAt)
                ORDER BY YEAR(b.createdAt) ASC
            """)
    List<BookingStatisticProjection> getCountByYear();

    @Query("""
                SELECT MONTH(b.createdAt) AS key, COUNT(b.createdAt) AS total
                FROM BookingEntity b
                WHERE YEAR(b.createdAt) = :year AND b.status = 'PAYED' AND b.deleted = false
                GROUP BY MONTH(b.createdAt)
                ORDER BY MONTH(b.createdAt) ASC
            """)
    List<BookingStatisticProjection> getCountByMonth(int year);
}
