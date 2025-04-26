package com.fasfood.common.dto.request;

import com.fasfood.common.enums.TransferType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequest extends Request {
    @NotNull(message = "TRANSACTION_ID_REQUIRED")
    private long id;
    @NotBlank(message = "GATEWAY_REQUIRED")
    private String gateway;
    @NotNull(message = "TRANSACTION_DATE_REQUIRED")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;
    @NotBlank(message = "ACCOUNT_NUMBER_REQUIRED")
    private String accountNumber;
    private String code;
    @NotBlank(message = "CONTENT_REQUIRED")
    private String content;
    @NotNull(message = "TRANSFER_TYPE_REQUIRED")
    private TransferType transferType;
    @NotNull(message = "TRANSFER_AMOUNT_REQUIRED")
    private Long transferAmount;
    private Long accumulated;
    private String subAccount;
    @NotNull(message = "REFERENCE_CODE_REQUIRED")
    private String referenceCode;
    private String description;
}
