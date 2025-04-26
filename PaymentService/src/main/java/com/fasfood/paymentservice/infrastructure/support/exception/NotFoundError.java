package com.fasfood.paymentservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotFoundError implements ResponseError {
    WALLET_COMMAND_NOTFOUND(4040001, "Không tìm thấy lệnh {0}"),
    WALLET_NOT_FOUND(4040002, "Không tìm thấy ví"),
    TRANSACTION_NOT_FOUND(4040003, "Không tìm thấy giao dịch {0}"),
    ;

    private final Integer code;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getStatus() {
        return 404;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
