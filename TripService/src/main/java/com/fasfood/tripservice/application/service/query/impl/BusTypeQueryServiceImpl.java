package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.tripservice.application.dto.mapper.BusTypeDTOMapper;
import com.fasfood.tripservice.application.dto.response.BusTypeResponse;
import com.fasfood.tripservice.application.service.query.BusTypeQueryService;
import com.fasfood.tripservice.domain.repository.BusTypeRepository;
import com.fasfood.tripservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.tripservice.infrastructure.support.util.BusTypeCacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusTypeQueryServiceImpl implements BusTypeQueryService {

    private final BusTypeRepository busTypeRepository;
    private final BusTypeDTOMapper busTypeDTOMapper;
    private final BusTypeCacher busTypeCacher;

    @Override
    public List<BusTypeDTO> getAll() {
        if (this.busTypeCacher.hasKey(BusTypeCacher.CACHE_NAME)) {
            return this.busTypeCacher.get(BusTypeCacher.CACHE_NAME).getTypes();
        }
        List<BusTypeDTO> result = this.busTypeDTOMapper.domainToDTO(this.busTypeRepository.findAll());
        this.busTypeCacher.put(BusTypeCacher.CACHE_NAME, new BusTypeResponse(result));
        return result;
    }

    @Override
    public BusTypeDTO getById(UUID id) {
        List<BusTypeDTO> result = this.getCached();
        if (Objects.isNull(result)) {
            return this.busTypeDTOMapper.domainToDTO(this.busTypeRepository.findById(id)
                    .orElseThrow(() -> new ResponseException(NotFoundError.BUS_TYPE_NOT_FOUND)));
        }
        BusTypeDTO found = null;
        for (BusTypeDTO busTypeDTO : result) {
            if (busTypeDTO.getId().equals(id)) {
                found = busTypeDTO;
                break;
            }
        }
        if (Objects.isNull(found)) {
            throw new ResponseException(NotFoundError.BUS_TYPE_NOT_FOUND);
        }
        return found;
    }

    private List<BusTypeDTO> getCached() {
        if (this.busTypeCacher.hasKey(BusTypeCacher.CACHE_NAME)) {
            return this.busTypeCacher.get(BusTypeCacher.CACHE_NAME).getTypes();
        }
        return null;
    }
}
