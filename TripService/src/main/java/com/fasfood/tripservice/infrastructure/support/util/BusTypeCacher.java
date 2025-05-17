package com.fasfood.tripservice.infrastructure.support.util;

import com.fasfood.tripservice.application.dto.response.BusTypeResponse;
import com.fasfood.client.support.Cacher;
import com.fasfood.client.support.DataCacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class BusTypeCacher implements Cacher<String, BusTypeResponse> {

    public static final String CACHE_NAME = "BUS_TYPE";

    private final DataCacher dataCacher;

    @Override
    public void put(String key, BusTypeResponse value, Duration duration) {
        this.dataCacher.put(key, value, null);
    }

    @Override
    public BusTypeResponse get(String key) {
        return this.dataCacher.get(key, BusTypeResponse.class);
    }

    @Override
    public void remove(String key) {
        this.dataCacher.remove(key);
    }

    @Override
    public boolean hasKey(String key) {
        return this.dataCacher.hasKey(key);
    }
}