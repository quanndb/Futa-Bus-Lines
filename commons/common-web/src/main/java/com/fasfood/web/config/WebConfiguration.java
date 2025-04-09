package com.fasfood.web.config;

import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfiguration implements ServletContextInitializer, WebMvcConfigurer {
    private final Logger log = LoggerFactory.getLogger(WebConfiguration.class);
    private final Environment env;

    public WebConfiguration(Environment env) {
        this.env = env;
    }

    public void onStartup(ServletContext servletContext) {
        if (this.env.getActiveProfiles().length != 0) {
            this.log.info("Web application configuration, using profiles: {}", Arrays.toString(this.env.getActiveProfiles()));
        }

        this.log.info("Web application fully configured");
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedHeaders(List.of("*")); // Cho phép tất cả headers
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setExposedHeaders(List.of("*")); // Expose tất cả headers
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        registry.addInterceptor(localeInterceptor);
        registry.addInterceptor(new ApiDocumentHandlerInterceptor());
    }
}
