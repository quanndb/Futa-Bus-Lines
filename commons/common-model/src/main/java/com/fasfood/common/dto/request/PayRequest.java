package com.fasfood.common.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayRequest extends Request {
    @NotNull(message = "USER_ID_REQUIRED")
    private UUID userId;
    @NotNull(message = "IS_USE_WALLET_REQUIRED")
    private Boolean isUseWallet;
    @NotNull(message = "ORDER_CODE_REQUIRED")
    private String code;
    @NotNull(message = "AMOUNT_REQUIRED")
    @Min(value = 10000, message = "AMOUNT_AT_LEAST")
    private long amount;
}
