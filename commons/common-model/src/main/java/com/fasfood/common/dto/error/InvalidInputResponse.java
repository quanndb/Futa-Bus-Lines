package com.fasfood.common.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class InvalidInputResponse extends ErrorResponse<Void> {
    private Set<FieldErrorResponse> errors;

    public InvalidInputResponse(int code, String message, String error, Set<FieldErrorResponse> errors) {
        super(code, message, null, error);
        this.errors = errors;
    }

    public InvalidInputResponse(int code, String message, String error) {
        super(code, message, null, error);
        this.errors = null;
    }
}
