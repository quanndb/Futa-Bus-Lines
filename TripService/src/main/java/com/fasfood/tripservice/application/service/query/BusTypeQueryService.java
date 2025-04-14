package com.fasfood.tripservice.application.service.query;

import com.fasfood.common.dto.response.BusTypeDTO;

import java.util.List;

public interface BusTypeQueryService {
    List<BusTypeDTO> getAll();
}
