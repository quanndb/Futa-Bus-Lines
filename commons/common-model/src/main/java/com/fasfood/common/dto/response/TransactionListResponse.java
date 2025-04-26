package com.fasfood.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TransactionListResponse extends SePayResponse {
    private List<TransactionDTO> transactions;

    public TransactionListResponse(int status, Exception exception) {
        super(status, exception);
        this.transactions = new ArrayList<>();
    }
}
