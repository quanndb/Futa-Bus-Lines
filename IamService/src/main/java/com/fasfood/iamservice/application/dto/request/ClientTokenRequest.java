package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientTokenRequest extends Request {
    private String clientId;
    private String clientSecret;
}
