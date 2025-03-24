package com.fasfood.web.config;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

public class ApiDocumentHandlerInterceptor implements HandlerInterceptor {
    public ApiDocumentHandlerInterceptor() {
    }

    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            Operation annotation = handlerMethod.getMethodAnnotation(Operation.class);
            if (annotation != null) {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                if (Objects.nonNull(requestAttributes)) {
                    requestAttributes.setAttribute("custom_api_doc", annotation.summary(), 0);
                }
            }
        }

        return true;
    }
}