package com.fasfood.common.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.common.enums.BusFloor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO extends AuditableDTO {
    private UUID id;
    private UUID busId;
    private String seatNumber;
    private BusFloor floor;
}
