package com.fasfood.common.error;

import lombok.Getter;

@Getter
public enum BadRequestError implements ResponseError {
    INVALID_INPUT(40000001, "Invalid input : {0}"),
    INVALID_ACCEPT_LANGUAGE(40000002, "Invalid value for request header Accept-Language: {0}"),
    MISSING_PATH_VARIABLE(40000003, "Missing path variable"),
    PATH_INVALID(40000004, "Path is invalid"),
    UNDEFINED(40000005, ""),
    FILE_SIZE_EXCEEDED(40000006, "File size exceeds the limit"),
    RECORD_IS_BEING_UPDATED(40000012, "The record is being updated. Please wait a minute"),
    INVALID_EMAIL(40000007, "Invalid email address"),
    // email
    TEMPLATE_CODE_REQUIRED(4000001, "Template code is required"),
    SENDER_REQUIRED(4000002, "Sender is required"),
    RECIPIENTS_REQUIRED(4000003, "Recipients are required"),
    SUBJECT_REQUIRED(4000004, "Subject is required"),
    BODY_REQUIRED(4000005, "Body is required"),
    // transaction
    TRANSACTION_ID_REQUIRED(4000006, "Transaction id is required"),
    GATEWAY_REQUIRED(4000007, "Gateway is required"),
    TRANSACTION_DATE_REQUIRED(4000008, "Transaction Date is required"),
    ACCOUNT_NUMBER_REQUIRED(4000009, "Account number is required"),
    CONTENT_REQUIRED(4000010, "Content is required"),
    TRANSFER_TYPE_REQUIRED(4000011, "Transfer type is required"),
    TRANSFER_AMOUNT_REQUIRED(4000012, "Transfer amount is required"),
    REFERENCE_CODE_REQUIRED(4000013, "Reference code is required"),
    // booking
    DETAILS_IDS_REQUIRED(4000014, "Details ids is required"),
    START_DATE_REQUIRED(4000015, "Start date is required"),
    ;

    private final Integer code;
    private final String message;

    BadRequestError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return this.name();
    }

    public int getStatus() {
        return 400;
    }

}
