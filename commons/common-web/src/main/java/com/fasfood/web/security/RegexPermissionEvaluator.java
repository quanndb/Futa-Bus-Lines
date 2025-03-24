package com.fasfood.web.security;

import com.fasfood.common.UserAuthentication;
import com.fasfood.common.error.AuthorizationError;
import com.fasfood.common.exception.ResponseException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.regex.Pattern;

@Component
public class RegexPermissionEvaluator implements PermissionEvaluator {

    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        String requiredPermission = permission.toString();
        if (authentication instanceof UserAuthentication userAuthentication) {
            if (userAuthentication.isRoot()) {
                return true;
            }
            else {
                return userAuthentication.getGrantedPermissions().stream().anyMatch((p) -> Pattern.matches(p, requiredPermission));
            }
        } else {
            throw new ResponseException(MessageFormat.format(AuthorizationError.NOT_SUPPORTED_AUTHENTICATION.getMessage(),
                    authentication.getClass().getName()), AuthorizationError.NOT_SUPPORTED_AUTHENTICATION,
                    authentication.getClass().getName());
        }
    }

    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return this.hasPermission(authentication, null, permission);
    }
}
