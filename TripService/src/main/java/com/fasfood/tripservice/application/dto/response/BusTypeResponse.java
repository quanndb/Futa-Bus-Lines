package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.response.BusTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BusTypeResponse {
    List<BusTypeDTO> types;
}
