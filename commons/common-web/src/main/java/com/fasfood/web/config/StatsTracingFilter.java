package com.fasfood.web.config;

import com.fasfood.web.support.SecurityUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@WebFilter({"/api/**"})
public class StatsTracingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(StatsTracingFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        Instant start = Instant.now();

        try {
            chain.doFilter(req, resp);
        } finally {
            if (resp.isCommitted()) {
                long time = Duration.between(start, Instant.now()).toMillis();
                this.logRequest((HttpServletRequest) req, (HttpServletResponse) resp, time);
            }
        }
    }

    private void logRequest(HttpServletRequest request, HttpServletResponse response, long time) {
        Optional<String> optionalCurrentUserId = SecurityUtils.getCurrentUser();
        if (optionalCurrentUserId.isPresent()) {
            log.info("User/client: {}, execute api: {}, status: {}, time: {} ms",
                    optionalCurrentUserId.get(), request.getRequestURI(), response.getStatus(), time);
        } else {
            log.info("Execute api: {}, status: {}, time: {} ms",
                    request.getRequestURI(), response.getStatus(), time);
        }
    }
}

