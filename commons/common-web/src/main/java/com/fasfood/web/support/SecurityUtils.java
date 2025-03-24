package com.fasfood.web.support;

import com.fasfood.common.UserAuthentication;
import com.fasfood.common.error.AuthenticationError;
import com.fasfood.common.exception.ResponseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SecurityUtils {
    public static Optional<String> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else {
            Object var3 = authentication.getPrincipal();
            if (var3 instanceof UserDetails springSecurityUser) {
                return springSecurityUser.getUsername();
            } else {
                var3 = authentication.getPrincipal();
                if (var3 instanceof String) {
                    return (String) var3;
                } else {
                    return null;
                }
            }
        }
    }

    public static Optional<UserAuthentication> getUserAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (Objects.nonNull(authentication) && authentication instanceof UserAuthentication userAuthentication) {
            return Optional.of(userAuthentication);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (Objects.nonNull(authentication) && authentication instanceof UserAuthentication userAuthentication) {
            return Optional.of(userAuthentication.getToken());
        } else {
            return Optional.empty();
        }
    }

    public static Optional<UUID> getCurrentUserLoginId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (Objects.nonNull(authentication) && authentication instanceof UserAuthentication userAuthentication) {
            return Objects.nonNull(userAuthentication.getUserId()) ? Optional.of(userAuthentication.getUserId()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    public static UserAuthentication authentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (Objects.nonNull(authentication) && authentication instanceof UserAuthentication userAuthentication) {
            return userAuthentication;
        } else {
            throw new ResponseException(AuthenticationError.UNAUTHORISED);
        }
    }

    public static UUID getUserId() {
        UserAuthentication userAuthentication = authentication();
        return userAuthentication.getUserId();
    }
}
