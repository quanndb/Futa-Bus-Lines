package com.fasfood.tripservice.infrastructure.domainrepository;

import com.fasfood.common.error.NotFoundError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.BusType;
import com.fasfood.tripservice.domain.Seat;
import com.fasfood.tripservice.domain.repository.BusTypeRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusTypeEntity;
import com.fasfood.tripservice.infrastructure.persistence.mapper.SeatEntityMapper;
import com.fasfood.tripservice.infrastructure.persistence.repository.BusTypeEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.SeatEntityRepository;
import com.fasfood.web.support.AbstractDomainRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BusTypeRepositoryImpl extends AbstractDomainRepository<BusType, BusTypeEntity, UUID> implements BusTypeRepository {

    private final BusTypeEntityRepository busTypeEntityRepository;
    private final SeatEntityRepository seatEntityRepository;
    private final SeatEntityMapper seatEntityMapper;

    protected BusTypeRepositoryImpl(JpaRepository<BusTypeEntity, UUID> jpaRepository,
                                    EntityMapper<BusType, BusTypeEntity> mapper,
                                    BusTypeEntityRepository busEntityRepository1,
                                    SeatEntityRepository seatEntityRepository,
                                    SeatEntityMapper seatEntityMapper) {
        super(jpaRepository, mapper);
        this.busTypeEntityRepository = busEntityRepository1;
        this.seatEntityRepository = seatEntityRepository;
        this.seatEntityMapper = seatEntityMapper;
    }


    @Override
    public BusType getById(UUID id) {
        BusType found = this.mapper.toDomain(this.busTypeEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.NOT_FOUND)));
        List<Seat> seats = this.seatEntityMapper.toDomain(this.seatEntityRepository.findAllByTypeId(id));
        found.enrichSeats(seats);
        return found;
    }

    @Override
    @Transactional
    public List<BusType> saveAll(List<BusType> domains) {
        List<Seat> seats = new ArrayList<>();
        domains.forEach(domain -> {
            seats.addAll(domain.getFirstFloorSeats());
            seats.addAll(domain.getSecondFloorSeats());
        });
        this.seatEntityRepository.saveAll(this.seatEntityMapper.toEntity(seats));
        return super.saveAll(domains);
    }

    @Override
    public List<BusType> findAll() {
        return this.enrichList(this.mapper.toDomain(this.busTypeEntityRepository.findAll()));
    }

    @Override
    protected List<BusType> enrichList(List<BusType> busTypes) {
        Map<UUID, List<Seat>> seatMap = this.seatEntityMapper
                .toDomain(this.seatEntityRepository.findAllByTypeIds(busTypes.stream().map(BusType::getId).toList()))
                .stream().collect(Collectors.groupingBy(Seat::getTypeId));
        busTypes.forEach(busType -> {
            if (seatMap.containsKey(busType.getId())) {
                busType.enrichSeats(seatMap.get(busType.getId()));
            }
        });
        return busTypes;
    }
}
