package com.fasfood.bookingservice.presentation;

import com.fasfood.web.config.AbstractSwaggerConfig;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression(value = "${springdoc.api-docs.enabled:false}")
public class SwaggerConfig extends AbstractSwaggerConfig {
    @Override
    protected Info metadata() {
        return new Info()
                .title("FasFood Booking Service")
                .description(
                        "FasFood Booking Service provides REST APIs to manage"
                                + " access management resources")
                .version("0.0.1");
    }
}
