package com.fasfood.iamservice.application.service;

import com.fasfood.common.UserAuthentication;
import com.fasfood.common.enums.TokenType;
import com.fasfood.common.error.AuthenticationError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.iamservice.application.dto.request.LoginRequest;
import com.fasfood.iamservice.application.dto.request.LogoutRequest;
import com.fasfood.iamservice.application.dto.response.LoginResponse;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountEntity;
import com.fasfood.iamservice.infrastructure.persistence.repository.AccountEntityRepository;
import com.fasfood.iamservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.iamservice.infrastructure.support.util.TokenProvider;
import com.fasfood.web.security.TokenCacheService;
import com.fasfood.web.support.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final AccountEntityRepository accountEntityRepository;
    private final TokenProvider tokenProvider;
    private final TokenCacheService tokenCacheService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        AccountEntity found = this.accountEntityRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseException(BadRequestError.EMAIL_NOT_FOUND));
        if(!this.passwordEncoder.matches(loginRequest.getPassword(), found.getPassword())) {
            throw new ResponseException(BadRequestError.WRONG_PASSWORD);
        }
        return this.tokenProvider.login(found.getId(), found.getEmail());
    }

    public void logout(LogoutRequest logoutRequest) {
        this.tokenCacheService.invalidToken(SecurityUtils.getCurrentUserJWT()
                .orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED)));
        this.tokenCacheService.invalidRefreshToken(logoutRequest.getRefreshToken());
    }

    public LoginResponse refresh() {
        String refreshToken = SecurityUtils.getCurrentUserJWT().orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED));
        UserAuthentication userAuthentication = SecurityUtils.getUserAuthentication().orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED));
        if(!TokenType.REFRESH_TOKEN.equals(userAuthentication.getTokenType())){
            throw new ResponseException(BadRequestError.MUST_HAVE_REFRESH_TOKEN);
        }
        return this.tokenProvider.refreshToken(userAuthentication.getUserId(), userAuthentication.getEmail(), refreshToken);
    }
}
