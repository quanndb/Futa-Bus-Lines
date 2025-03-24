//package com.fasfood.client.config;
//
//import com.fasfood.client.config.security.ClientAuthentication;
//import com.fasfood.common.dto.request.ClientRequest;
//import com.fasfood.common.dto.response.ClientResponse;
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.Getter;
//import lombok.Setter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Collection;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Map;
//
//@Getter
//@Setter
//public class FeignClientInterceptor implements RequestInterceptor {
//    private static final Logger log = LoggerFactory.getLogger(FeignClientInterceptor.class);
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String TOKEN_TYPE = "Bearer";
//    private static final String URI_CLIENT_AUTHENTICATE = "/api/client/authenticate";
//    private static final String CLIENT_CREDENTIALS = "client_credentials";
//    @Value("${app.security.client.id}")
//    private String clientId;
//    @Value("${app.security.client.client-secret}")
//    private String clientSecret;
//    private ClientAuthentication clientAuthentication;
//
//    public FeignClientInterceptor() {
//        log.info("FeignClientConfiguration");
//    }
//
//    public void apply(RequestTemplate requestTemplate) {
//        log.debug("FeignClientInterceptor start apply request");
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attributes != null) {
//            HttpServletRequest request = attributes.getRequest();
//            Enumeration headerNames = request.getHeaderNames();
//            if (headerNames != null) {
//                while (headerNames.hasMoreElements()) {
//                    String headerName = (String) headerNames.nextElement();
//                    String headerValue = request.getHeader(headerName);
//                    requestTemplate.header(headerName, headerValue);
//                }
//            }
//        }
//
//        Map<String, Collection<String>> headers = requestTemplate.headers();
//        List<String> authorization = (List<String>) headers.get(AUTHORIZATION_HEADER);
//        if (authorization == null || !authorization.getFirst().startsWith(TOKEN_TYPE)) {
//            ClientResponse clientTokenResponse = this.clientAuthentication
//                    .getClientToken(new ClientRequest(this.clientId, this.clientSecret, CLIENT_CREDENTIALS));
//            if (clientTokenResponse != null) {
//                requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, clientTokenResponse.getAccessToken()));
//            }
//        }
//    }
//}