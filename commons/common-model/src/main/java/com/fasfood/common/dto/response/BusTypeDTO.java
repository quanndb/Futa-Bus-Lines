package com.fasfood.common.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.common.enums.BusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusTypeDTO extends AuditableDTO {
    private UUID id;
    private BusTypeEnum type;
    private int seatCapacity;
    private List<SeatDTO> firstFloorSeats;
    private List<SeatDTO> secondFloorSeats;
}
