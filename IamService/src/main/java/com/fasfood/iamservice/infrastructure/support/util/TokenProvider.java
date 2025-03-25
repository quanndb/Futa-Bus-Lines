package com.fasfood.iamservice.infrastructure.support.util;

import com.fasfood.common.enums.TokenType;
import com.fasfood.iamservice.application.dto.response.LoginResponse;
import com.fasfood.iamservice.infrastructure.persistence.readmodel.AuthenticationProperties;
import com.fasfood.iamservice.infrastructure.persistence.readmodel.TokenLifeTimeProperties;
import com.fasfood.util.TimeConverter;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({AuthenticationProperties.class, TokenLifeTimeProperties.class})
public class TokenProvider implements InitializingBean {

    private KeyPair keyPair;
    private final AuthenticationProperties properties;
    private final TokenLifeTimeProperties lifeTimeProperties;

    @Override
    public void afterPropertiesSet() {
        this.keyPair = this.getKeyPair(this.properties.getName(),
                this.properties.getPassword(),
                this.properties.getAlias());
    }

    private KeyPair getKeyPair(String keyStore, String password, String alias) {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyStore), password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(alias);
    }

    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) this.keyPair.getPublic()).keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(UUID.randomUUID().toString());
        return new JWKSet(builder.build());
    }

    public LoginResponse login(UUID uid, String email) {
        return LoginResponse.builder()
                .accessToken(this.accessTokenFactory(uid, email, TimeConverter
                        .convertToMilliseconds(this.lifeTimeProperties.getAccessTokenLifetime()), TokenType.ACCESS_TOKEN))
                .refreshToken(this.accessTokenFactory(uid, email, TimeConverter
                        .convertToMilliseconds(this.lifeTimeProperties.getRefreshTokenLifetime()), TokenType.REFRESH_TOKEN))
                .tokenExpiresIn(this.lifeTimeProperties.getAccessTokenLifetime())
                .refreshExpiresIn(this.lifeTimeProperties.getRefreshTokenLifetime())
                .tokenType("Bearer")
                .build();
    }

    public LoginResponse refreshToken(UUID uid, String email, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(this.accessTokenFactory(uid, email, TimeConverter
                        .convertToMilliseconds(this.lifeTimeProperties.getAccessTokenLifetime()), TokenType.ACCESS_TOKEN))
                .refreshToken(refreshToken)
                .tokenExpiresIn(this.lifeTimeProperties.getAccessTokenLifetime())
                .refreshExpiresIn(this.lifeTimeProperties.getRefreshTokenLifetime())
                .tokenType("Bearer")
                .build();
    }

    public String actionToken(UUID uid, String email) {
        return this.accessTokenFactory(uid, email,
                TimeConverter.convertToMilliseconds(this.lifeTimeProperties.getActionTokenLifetime()),
                TokenType.ACTION_TOKEN);
    }

    private String accessTokenFactory(UUID uid, String email, long lifeTime, TokenType type) {
        // build token
        return Jwts.builder()
                .subject(uid.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + lifeTime))
                .claim("jti", UUID.randomUUID().toString())
                .claim("email", email)
                .claim("type", type.name())
                .signWith(this.keyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();
    }
}
