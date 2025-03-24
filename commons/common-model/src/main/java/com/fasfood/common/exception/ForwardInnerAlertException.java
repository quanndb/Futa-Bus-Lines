package com.fasfood.common.exception;

import com.fasfood.common.dto.error.ErrorResponse;
import lombok.Getter;

import java.io.Serial;

@Getter
public class ForwardInnerAlertException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ErrorResponse<Void> response;

    public ForwardInnerAlertException(ErrorResponse<Void> response) {
        this.response = response;
    }
}