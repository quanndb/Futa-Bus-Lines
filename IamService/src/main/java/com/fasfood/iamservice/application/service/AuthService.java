package com.fasfood.iamservice.application.service;

import com.fasfood.client.client.google.GoogleClient;
import com.fasfood.client.client.google.GoogleOAuthClient;
import com.fasfood.client.client.notification.NotificationClient;
import com.fasfood.common.UserAuthentication;
import com.fasfood.common.constant.EmailTitleReadModel;
import com.fasfood.common.dto.request.GetGoogleTokenRequest;
import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.common.dto.response.GoogleTokenResponse;
import com.fasfood.common.dto.response.GoogleUserResponse;
import com.fasfood.common.enums.AccountStatus;
import com.fasfood.common.enums.TokenType;
import com.fasfood.common.error.AuthenticationError;
import com.fasfood.common.error.InternalServerError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.iamservice.application.dto.request.LoginRequest;
import com.fasfood.iamservice.application.dto.request.LogoutRequest;
import com.fasfood.iamservice.application.dto.response.LoginResponse;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountEntity;
import com.fasfood.iamservice.infrastructure.persistence.repository.AccountEntityRepository;
import com.fasfood.iamservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.iamservice.infrastructure.support.util.MacCache;
import com.fasfood.iamservice.infrastructure.support.util.TokenProvider;
import com.fasfood.util.IdUtils;
import com.fasfood.util.MacAddressUtil;
import com.fasfood.util.StrUtils;
import com.fasfood.web.security.TokenCacheService;
import com.fasfood.web.support.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
@Primary
@EnableConfigurationProperties({GetGoogleTokenRequest.class})
public class AuthService {

    @Value(value = "${app.client.return-url}")
    private String RETURN_URL;
    private final AccountEntityRepository accountEntityRepository;
    private final TokenProvider tokenProvider;
    private final TokenCacheService tokenCacheService;
    private final MacCache macCache;
    private final PasswordEncoder passwordEncoder;
    private final GoogleClient googleClient;
    private final GoogleOAuthClient googleOAuthClient;
    private final NotificationClient notificationClient;
    private final GetGoogleTokenRequest getGoogleTokenRequest;

    public LoginResponse login(LoginRequest loginRequest) throws JsonProcessingException {
        AccountEntity found = this.accountEntityRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseException(BadRequestError.EMAIL_NOT_FOUND));
        if (!AccountStatus.ACTIVE.equals(found.getStatus())) {
            throw new ResponseException(BadRequestError.ACCOUNT_NOT_ACTIVE);
        }
        if (!this.passwordEncoder.matches(loginRequest.getPassword(), found.getPassword())) {
            throw new ResponseException(BadRequestError.WRONG_PASSWORD);
        }
        return this.processLogin(found);
    }

    public LoginResponse loginWithGoogle(String code) throws JsonProcessingException {
        this.getGoogleTokenRequest.setCode(code);
        GoogleTokenResponse response = this.googleOAuthClient.getToken(this.getGoogleTokenRequest);
        if (Objects.isNull(response)) {
            throw new ResponseException(InternalServerError.INVALID_AUTH_CODE);
        }
        GoogleUserResponse userResponse = this.googleClient.getUserInfo(String.join(" ", "Bearer", response.getAccessToken()));
        if (Objects.isNull(userResponse)) {
            throw new ResponseException(InternalServerError.INVALID_TOKEN);
        }
        AccountEntity account = this.accountEntityRepository.findByEmail(userResponse.getEmail())
                .orElseGet(() -> this.accountEntityRepository.save(AccountEntity.builder()
                        .email(userResponse.getEmail())
                        .id(IdUtils.nextId())
                        .password(this.passwordEncoder.encode(IdUtils.nextId().toString()))
                        .avatarUrl(userResponse.getPicture())
                        .fullName(userResponse.getName())
                        .status(AccountStatus.ACTIVE)
                        .deleted(false)
                        .build()));
        return this.processLogin(account);
    }

    public void verifyMac() {
        UserAuthentication authentication = SecurityUtils.getUserAuthentication()
                .orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED));
        if (!TokenType.ACTION_TOKEN.equals(authentication.getTokenType())) {
            throw new ResponseException(BadRequestError.MUST_HAS_ACTION_TOKEN);
        }
        AccountEntity found = this.accountEntityRepository.findById(authentication.getUserId())
                .orElseThrow(() -> new ResponseException(BadRequestError.EMAIL_NOT_FOUND));
        this.macCache.put(StrUtils.joinKey(found.getId().toString()), MacAddressUtil.getMacAddress());
    }

    public void logout(LogoutRequest logoutRequest) {
        this.tokenCacheService.invalidToken(SecurityUtils.getCurrentUserJWT()
                .orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED)));
        this.tokenCacheService.invalidRefreshToken(logoutRequest.getRefreshToken());
    }

    public LoginResponse refresh() {
        String refreshToken = SecurityUtils.getCurrentUserJWT().orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED));
        UserAuthentication userAuthentication = SecurityUtils.getUserAuthentication().orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED));
        if (!TokenType.REFRESH_TOKEN.equals(userAuthentication.getTokenType())) {
            throw new ResponseException(BadRequestError.MUST_HAVE_REFRESH_TOKEN);
        }
        AccountEntity account = this.accountEntityRepository.findByEmail(userAuthentication.getEmail())
                .orElseThrow(() -> new ResponseException(BadRequestError.EMAIL_NOT_FOUND));
        return this.tokenProvider.refreshToken(account, refreshToken);
    }

    private LoginResponse processLogin(AccountEntity account) throws JsonProcessingException {
        if (!AccountStatus.ACTIVE.equals(account.getStatus())) {
            throw new ResponseException(BadRequestError.ACCOUNT_NOT_ACTIVE);
        }
        // MAC check
        if (!this.macCache.hasKey(StrUtils.joinKey(account.getId().toString(), MacAddressUtil.getMacAddress()))) {
            log.error(String.join(":", MacCache.MAC_KEY, account.getId().toString(), MacAddressUtil.getMacAddress()));
            this.notificationClient.send(SendEmailRequest.builder()
                    .to(List.of(account.getEmail()))
                    .subject(EmailTitleReadModel.TWO_FA)
                    .templateCode("TP-M9WSYJP6K98")
                    .variables(Map.of("name", account.getFullName(),
                            "address", Objects.requireNonNull(MacAddressUtil.getMacAddress()),
                            "resetLink", String.join("", this.RETURN_URL, "/actions/mac-verification?token=", this.tokenProvider.actionToken(account))))
                    .build()).getData();
            throw new ResponseException(BadRequestError.INVALID_MAC_ADDRESS, MacAddressUtil.getMacAddress());
        }
        return this.tokenProvider.login(account);
    }
}
