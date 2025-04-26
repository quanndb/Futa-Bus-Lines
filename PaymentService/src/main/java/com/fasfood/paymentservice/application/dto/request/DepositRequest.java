package com.fasfood.paymentservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest extends Request {
    @NotNull(message = "AMOUNT_REQUIRED")
    @Min(value = 10000, message = "AMOUNT_AT_LEAST")
    private long amount;
}
