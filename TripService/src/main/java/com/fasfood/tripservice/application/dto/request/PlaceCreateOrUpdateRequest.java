package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceCreateOrUpdateRequest extends Request {
    @NotBlank(message = "NAME_REQUIRED")
    private String name;
    @NotBlank(message = "ADDRESS_REQUIRED")
    private String address;
    @NotBlank(message = "CODE_REQUIRED")
    private String code;
}
