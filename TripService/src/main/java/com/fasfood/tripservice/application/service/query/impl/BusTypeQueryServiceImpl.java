package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.tripservice.application.dto.mapper.BusTypeDTOMapper;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.tripservice.application.dto.response.BusTypeResponse;
import com.fasfood.tripservice.application.service.query.BusTypeQueryService;
import com.fasfood.tripservice.domain.repository.BusTypeRepository;
import com.fasfood.tripservice.infrastructure.support.util.BusTypeCacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
