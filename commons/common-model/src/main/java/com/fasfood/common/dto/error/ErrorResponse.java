package com.fasfood.common.dto.error;

import com.fasfood.common.dto.response.Response;
import com.fasfood.common.enums.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> extends Response<T> {
    private String error;

    public ErrorResponse(int code, String message, T data, String error) {
        this.setData(data);
        this.setCode(code);
        this.setMessage(message);
        this.setSuccess(false);
        this.setStatus(ResponseCode.FAIL.name());
        this.error = error;
    }
}
