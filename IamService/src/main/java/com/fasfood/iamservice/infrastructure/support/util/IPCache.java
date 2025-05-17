package com.fasfood.iamservice.infrastructure.support.util;

import com.fasfood.util.StrUtils;
import com.fasfood.client.support.Cacher;
import com.fasfood.client.support.DataCacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class IPCache implements Cacher<String, String> {

    public static final String IP_KEY = "IP";
    private final DataCacher dataCacher;

    @Override
    public void put(String key, String value, Duration duration) {
        this.dataCacher.put(StrUtils.joinKey(IP_KEY, key, value), value, null);
    }

    @Override
    public String get(String key) {
        return this.dataCacher.get(StrUtils.joinKey(IP_KEY, key), String.class);
    }

    @Override
    public void remove(String key) {
        this.dataCacher.remove(StrUtils.joinKey(IP_KEY, key));
    }

    @Override
    public boolean hasKey(String key) {
        return this.dataCacher.hasKey(StrUtils.joinKey(IP_KEY, key));
    }
}
