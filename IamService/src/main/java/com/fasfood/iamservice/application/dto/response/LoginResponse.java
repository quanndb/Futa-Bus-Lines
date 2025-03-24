package com.fasfood.iamservice.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String tokenExpiresIn;
    private String refreshExpiresIn;
}
