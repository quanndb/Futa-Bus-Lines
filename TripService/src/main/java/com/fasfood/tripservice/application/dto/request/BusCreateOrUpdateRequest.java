package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.tripservice.infrastructure.support.enums.BusStatus;
import com.fasfood.common.enums.BusTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusCreateOrUpdateRequest extends Request {
    @NotBlank(message = "LICENSE_PLATE_REQUIRED")
    private String licensePlate;
    @NotNull(message = "BUS_TYPE_REQUIRED")
    private BusTypeEnum type;
    private BusStatus status;
}
