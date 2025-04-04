package com.fasfood.client.config;

import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class FeignClientInterceptor implements RequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(FeignClientInterceptor.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    private static final String TOKEN_TYPE = "Bearer";
    private ClientAuthentication clientAuthentication;
    private final ClientRequest clientRequest;

    public FeignClientInterceptor(ClientRequest clientRequest, ClientAuthentication clientAuthentication) {
        log.info("FeignClientConfiguration");
        this.clientRequest = clientRequest;
        this.clientAuthentication = clientAuthentication;
    }

    public void apply(RequestTemplate requestTemplate) {
        log.debug("Applying Feign request interceptor");
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            Map<String, String> headers = new HashMap<>();
            var request = attributes.getRequest();
            var headersStr = request.getHeaderNames();
            while (headersStr.hasMoreElements()) {
                var headerName = headersStr.nextElement();
                headers.put(headerName, request.getHeader(headerName));
            }
            String authorization = headers.get(AUTHORIZATION_HEADER);
            if (authorization == null || !authorization.startsWith(TOKEN_TYPE)) {
                ClientResponse clientToken = this.clientAuthentication.getClientToken(this.clientRequest);
                if (Objects.nonNull(clientToken)) {
                    requestTemplate.header(AUTHORIZATION_HEADER, String.join(" ", TOKEN_TYPE, clientToken.getAccessToken()));
                }

            } else {
                headers.forEach((key, value) -> {
                    if ((!Objects.equals(key, CONTENT_LENGTH_HEADER)
                            && !Objects.equals(key, CONTENT_TYPE_HEADER))
                            && Objects.nonNull(value)) {
                        requestTemplate.header(key, value);
                    }
                });
            }
        }
    }
}