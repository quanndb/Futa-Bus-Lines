package com.fasfood.tripservice.application.service.cmd.impl;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.tripservice.application.dto.mapper.BusTypeDTOMapper;
import com.fasfood.tripservice.application.dto.request.BusTypeCreateOrUpdateRequest;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.tripservice.application.mapper.BusTypeCommandMapper;
import com.fasfood.tripservice.application.service.cmd.BusTypeCommandService;
import com.fasfood.tripservice.domain.BusType;
import com.fasfood.tripservice.domain.cmd.BusTypeCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.repository.BusTypeRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.BusTypeEntityRepository;
import com.fasfood.tripservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.tripservice.infrastructure.support.util.BusTypeCacher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusTypeCommandServiceImpl implements BusTypeCommandService {

    private final BusTypeEntityRepository busTypeEntityRepository;
    private final BusTypeRepository busTypeRepository;
    private final BusTypeDTOMapper busTypeDTOMapper;
    private final BusTypeCommandMapper busTypeCommandMapper;
    private final BusTypeCacher busTypeCacher;

    @Override
    @Transactional
    public BusTypeDTO create(BusTypeCreateOrUpdateRequest request) {
        this.busTypeEntityRepository.findByCode(request.getType()).ifPresent(entity -> {
            throw new ResponseException(BadRequestError.EXISTED_BUS_TYPE, request.getType());
        });
        BusTypeCreateOrUpdateCmd cmd = this.busTypeCommandMapper.cmdFromRequest(request);
        BusType saved = this.busTypeRepository.save(new BusType(cmd));
        if (Objects.nonNull(saved)) {
            this.busTypeCacher.remove(BusTypeCacher.CACHE_NAME);
        }
        return this.busTypeDTOMapper.domainToDTO(saved);
    }

    @Override
    @Transactional
    public BusTypeDTO update(UUID id, BusTypeCreateOrUpdateRequest request) {
        BusType found = this.busTypeRepository.getById(id);
        this.busTypeEntityRepository.findByCodeExcept(request.getType(), found.getType()).ifPresent(entity -> {
            throw new ResponseException(BadRequestError.EXISTED_BUS_TYPE, request.getType());
        });
        BusTypeCreateOrUpdateCmd cmd = this.busTypeCommandMapper.cmdFromRequest(request);
        found = this.busTypeRepository.save(found.update(cmd));
        if (Objects.nonNull(found)) {
            this.busTypeCacher.remove(BusTypeCacher.CACHE_NAME);
        }
        return this.busTypeDTOMapper.domainToDTO(found);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        BusType found = this.busTypeRepository.getById(id);
        found.delete();
        this.busTypeCacher.remove(BusTypeCacher.CACHE_NAME);
        this.busTypeRepository.save(found);
    }
}
