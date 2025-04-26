package com.fasfood.paymentservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.common.enums.TransferType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TransactionPagingRequest extends PagingRequest {
    private LocalDate transactionDate;
    private List<Long> transactionIds;
    private List<TransferType> transferTypes;
}
