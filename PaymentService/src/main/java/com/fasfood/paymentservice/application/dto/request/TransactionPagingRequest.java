package com.fasfood.paymentservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.common.enums.TransferType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TransactionPagingRequest extends PagingRequest {
    private LocalDate transactionDate;
    @NotNull(message = "START_DATE_REQUIRED")
    private LocalDate startDate;
    @NotNull(message = "END_DATE_REQUIRED")
    private LocalDate endDate;
    private List<Long> transactionIds;
    private List<TransferType> transferTypes;
}
