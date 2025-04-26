package com.fasfood.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDetailsResponse extends SePayResponse{
    private TransactionDTO transaction;

    public TransactionDetailsResponse(int status, Exception exception){
        super(status, exception);
        this.transaction = null;
    }
}
