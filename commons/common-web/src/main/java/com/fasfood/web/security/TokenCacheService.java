package com.fasfood.web.security;

public interface TokenCacheService {
    String INVALID_TOKEN_CACHE = "invalid-access-token";
    String INVALID_REFRESH_TOKEN_CACHE = "invalid-refresh-token";
    String INVALID_SID_CACHE = "invalid-jti";

    void invalidToken(String token);

    void invalidSid(String sid);

    void invalidRefreshToken(String refreshToken);

    default boolean isInvalidToken(String token) {
        return this.isExisted(INVALID_TOKEN_CACHE, token);
    }

    default boolean isInvalidSid(String sid) {
        return this.isExisted(INVALID_SID_CACHE, sid);
    }

    boolean isExisted(String cacheName, String token);

    default boolean isInvalidRefreshToken(String refreshToken) {
        return this.isExisted(INVALID_REFRESH_TOKEN_CACHE, refreshToken);
    }
}
