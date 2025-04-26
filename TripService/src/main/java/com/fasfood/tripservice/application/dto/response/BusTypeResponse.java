package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.response.BusTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusTypeResponse {
    List<BusTypeDTO> types;
}
