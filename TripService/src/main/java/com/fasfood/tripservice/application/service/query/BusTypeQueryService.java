package com.fasfood.tripservice.application.service.query;

import com.fasfood.common.dto.response.BusTypeDTO;

import java.util.List;
import java.util.UUID;

public interface BusTypeQueryService {
    List<BusTypeDTO> getAll();

    BusTypeDTO getById(UUID id);
}
