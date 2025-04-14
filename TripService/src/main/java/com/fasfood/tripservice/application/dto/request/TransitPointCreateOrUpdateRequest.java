package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.tripservice.infrastructure.support.enums.TransitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitPointCreateOrUpdateRequest extends Request {
    @NotBlank(message = "TRANSIT_NAME_REQUIRED")
    private String name;
    @NotBlank(message = "TRANSIT_ADDRESS_REQUIRED")
    private String address;
    private String hotline;
    @NotNull(message = "TRANSIT_TYPE_REQUIRED")
    private TransitType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransitPointCreateOrUpdateRequest that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
