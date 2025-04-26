package com.fasfood.paymentservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithDrawCreateOrUpdateRequest extends Request {
    @NotBlank(message = "BANK_CODE_REQUIRED")
    private String bankCode;
    @NotBlank(message = "ACCOUNT_NUMBER_REQUIRED")
    private String accountNumber;
    @NotBlank(message = "RECEIVER_NAME_REQUIRED")
    private String receiverName;
    @NotNull(message = "AMOUNT_REQUIRED")
    @Min(value = 10000, message = "AMOUNT_AT_LEAST")
    private Long amount;
}
