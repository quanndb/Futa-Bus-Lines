package com.fasfood.iamservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BadRequestError implements ResponseError {
    INVALID_EMAIL(401000, "Invalid email address"),
    EMAIL_REQUIRED(401001, "Email is required"),
    PASSWORD_REQUIRED(401002, "Password is required"),
    PASSWORD_AT_LEAST(401003, "Password at least 8 characters"),
    EMAIL_NOT_FOUND(401004, "Email not found"),
    WRONG_PASSWORD(401005, "Wrong password"),
    MUST_HAVE_REFRESH_TOKEN(401006, "Must have refresh token"),
    ROLE_REQUIRED(401007, "Role is required"),
    FULL_NAME_REQUIRED(401008, "Full name is required"),
    OLD_PASSWORD_REQUIRED(401009, "Old password is required"),
    NEW_PASSWORD_REQUIRED(4010010, "New password is required"),
    PASSWORD_MUST_BE_DIFFERENT(4010011, "Password must be different"),
    PERMISSION_NAME_REQUIRED(4010012, "Permission name is required"),
    PERMISSION_CODE_REQUIRED(4010013, "Permission code is required"),
    PERMISSION_REQUIRED(4010014, "Permission is required"),
    SCOPE_REQUIRED(4010015, "Scope is required"),
    ROLE_NAME_REQUIRED(4010016, "Role name is required"),
    IS_ROOT_REQUIRED(4010017, "Is root user"),
    ROLE_EXISTED(4010018, "Role already existed: {0}"),
    PERMISSION_EXISTED(4010019, "Permission already existed: {0}"),
    EMAIL_EXISTED(4010020, "Email already existed: {0}"),
    INVALID_AVATAR(4010021, "Invalid avatar"),
    INVALID_IP_ADDRESS(4010022, "Địa chỉ IP không hợp lệ: {0}, vui lòng kiểm tra email"),
    MUST_HAS_ACTION_TOKEN(4010023, "Must have action token"),
    ACCOUNT_NOT_ACTIVE(4010024, "Account is not active"),
    ;

    private final Integer code;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}