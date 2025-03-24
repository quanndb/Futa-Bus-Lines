package com.fasfood.common.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagingRequest extends Request {
    public static final String ASC_SYMBOL = "ASC";
    public static final String DESC_SYMBOL = "DESC";
    protected @Min(
            value = 1L,
            message = "Page index must be greater than 0"
    )
    @Max(
            value = 1000L,
            message = "Page index be less than 1000"
    ) int pageIndex = 1;
    protected @Min(
            value = 1L,
            message = "Page size must be greater than 0"
    )
    @Max(
            value = 500L,
            message = "Page size must be less than or equal to 500"
    ) int pageSize = 30;
    protected String sortBy;
    protected String keyword;
    protected List<UUID> ids;
}
