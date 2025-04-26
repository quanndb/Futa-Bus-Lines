package com.fasfood.paymentservice.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfig {
    @Bean
    public PayOS payos(@Value("${app.payos.client-id}") String clientId,
                       @Value("${app.payos.api-key}") String apiSecret,
                       @Value("${app.payos.checksum-key}") String checksumKey){
        return new PayOS(clientId, apiSecret, checksumKey);
    }
}
