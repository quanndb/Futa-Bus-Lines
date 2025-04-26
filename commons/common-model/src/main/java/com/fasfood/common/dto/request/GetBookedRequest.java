package com.fasfood.common.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBookedRequest {
    @NotEmpty(message = "DETAILS_IDS_REQUIRED")
    private List<UUID> detailsIds;
    @NotNull(message = "START_DATE_REQUIRED")
    private LocalDate startDate;
}
