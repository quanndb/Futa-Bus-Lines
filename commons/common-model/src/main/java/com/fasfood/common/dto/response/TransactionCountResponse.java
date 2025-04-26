package com.fasfood.common.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionCountResponse extends SePayResponse {
    private long countTransactions;

    public TransactionCountResponse(int status, Exception exception) {
        super(status, exception);
        this.countTransactions = 0;
    }
}
