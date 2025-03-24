package com.fasfood.iamservice.presentation.rest.impl;

import com.fasfood.iamservice.infrastructure.support.util.TokenProvider;
import com.fasfood.iamservice.presentation.rest.JwkController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwkControllerImpl implements JwkController {

    private final TokenProvider tokenProvider;

    @Override
    public Map<String, Object> keys() {
        return this.tokenProvider.jwkSet().toJSONObject();
    }
}
