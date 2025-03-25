package com.fasfood.iamservice.infrastructure.support.util;

import com.fasfood.common.UserAuthority;
import com.fasfood.util.StrUtils;
import com.fasfood.web.support.Cacher;
import com.fasfood.web.support.DataCacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAuthoritiesCache implements Cacher<UUID, UserAuthority> {
    public static final String AUTHORITIES_KEY = "authorities";
    private final DataCacher dataCacher;

    @Override
    public void put(UUID key, UserAuthority value, Duration duration) {
        this.dataCacher.put(StrUtils.joinKey(AUTHORITIES_KEY, key.toString()), value, null);
    }

    @Override
    public UserAuthority get(UUID key) {
        return this.dataCacher.get(StrUtils.joinKey(AUTHORITIES_KEY, key.toString()), UserAuthority.class);
    }

    @Override
    public void remove(UUID key) {
        this.dataCacher.remove(StrUtils.joinKey(AUTHORITIES_KEY, key.toString()));
    }

    @Override
    public boolean hasKey(UUID key) {
        return this.dataCacher.hasKey(StrUtils.joinKey(AUTHORITIES_KEY, key.toString()));
    }
}
