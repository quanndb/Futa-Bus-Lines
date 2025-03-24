package com.fasfood.apigateway.fallback;

import com.fasfood.apigateway.dto.Response;
import com.fasfood.apigateway.error.InternalServerError;
import com.fasfood.apigateway.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FallbackController {

    @RequestMapping("fallback")
    public Response<Void> fallback() {
        throw new ResponseException(InternalServerError.SERVICE_UNAVAILABLE_ERROR);
    }
}
