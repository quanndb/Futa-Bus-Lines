package com.fasfood.web.exceptionHandler;

import com.fasfood.common.dto.error.ErrorResponse;
import com.fasfood.common.dto.error.FieldErrorResponse;
import com.fasfood.common.dto.error.InvalidInputResponse;
import com.fasfood.common.error.AuthenticationError;
import com.fasfood.common.error.AuthorizationError;
import com.fasfood.common.error.BadRequestError;
import com.fasfood.common.error.InternalServerError;
import com.fasfood.common.error.NotFoundError;
import com.fasfood.common.error.ResponseError;
import com.fasfood.common.error.ServiceUnavailableError;
import com.fasfood.common.exception.ForwardInnerAlertException;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.validator.ExceptionTranslator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Order
@RequiredArgsConstructor
public class ExceptionHandleAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandleAdvice.class);
    private static final String FAILED_TO_HANDLE_REQUEST = "Failed to handle request ";
    private static final String INVALID_REQUEST_ARGUMENTS = "Invalid request arguments";
    private final ExceptionTranslator exceptionTranslator;

    @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<ErrorResponse<Void>> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException e, HttpServletRequest request) {
        String var10001 = request.getRequestURI();
        log.warn("Failed to handle request {}: {}", var10001, e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.LOCKED).body(new InvalidInputResponse(HttpStatus.LOCKED.value(), this.getMessage(BadRequestError.RECORD_IS_BEING_UPDATED.getName(), "Transaction is being locked", new Object[0]), BadRequestError.RECORD_IS_BEING_UPDATED.getName()));
    }

    private void catchException(Exception exception) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            requestAttributes.setAttribute("custom_exception_message", exception, 0);
        }
    }

    private String getMessage(String value, String defaultMessage, Object... args) {
        if (Objects.nonNull(defaultMessage)) {
            return MessageFormat.format(defaultMessage, args);
        }
        return MessageFormat.format(value, args);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String var10001 = request.getRequestURI();
        log.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder().field(e.getParameter().getParameterName()).message(e.getMessage()).build());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.getName(), errors));
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<ErrorResponse<Void>> handleIllegalStateException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        String var10001 = request.getRequestURI();
        log.warn("Failed to handle request file Upload{}: {}", var10001, e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.FILE_SIZE_EXCEEDED.getName(), BadRequestError.FILE_SIZE_EXCEEDED.getMessage(), new Object[0]), BadRequestError.INVALID_INPUT.getName(), errors));
    }

    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity<ErrorResponse<Void>> handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String var10001 = request.getRequestURI();
        log.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder().field(e.getParameter().getParameterName()).message("Missing path variable").build());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(BadRequestError.MISSING_PATH_VARIABLE.getCode(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.MISSING_PATH_VARIABLE.getName(), errors));
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<ErrorResponse<Void>> handleMissingRequestHeaderException(MissingRequestHeaderException e, HttpServletRequest request) {
        String var10001 = request.getRequestURI();
        log.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder().field(e.getParameter().getParameterName()).message("Missing request header").build());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(BadRequestError.MISSING_PATH_VARIABLE.getCode(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.MISSING_PATH_VARIABLE.getName(), errors));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse<Void>> handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String var10001 = request.getRequestURI();
        log.warn("Failed to handle request " + var10001 + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder().field(e.getMethod()).message("Http request method not support").build());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new InvalidInputResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.getName(), errors));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(ConstraintViolationException e, HttpServletRequest request) {
        Set<FieldErrorResponse> errors = new HashSet<>();

        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String queryParamPath = constraintViolation.getPropertyPath().toString();
            log.debug("queryParamPath = {}", queryParamPath);
            String queryParam = queryParamPath.contains(".") ? queryParamPath.substring(queryParamPath.indexOf(".") + 1) : queryParamPath;
            String object = queryParamPath.split("\\.").length > 1 ? queryParamPath.substring(queryParamPath.indexOf(".") + 1, queryParamPath.lastIndexOf(".")) : queryParamPath;
            String errorMessage = this.getMessage(constraintViolation.getMessage(), constraintViolation.getMessage(), new Object[0]);
            errors.add(FieldErrorResponse.builder().field(queryParam).objectName(object).message(errorMessage).build());
        }

        InvalidInputResponse invalidInputResponse;
        if (!CollectionUtils.isEmpty(errors)) {
            long count = (long) errors.size();
            invalidInputResponse = (InvalidInputResponse) errors.stream().skip(count - 1L).findFirst().map((fieldErrorResponse) -> new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), fieldErrorResponse.getObjectName(), errors)).orElse((InvalidInputResponse) null);
        } else {
            invalidInputResponse = new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.getName(), errors);
        }

        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidInputResponse);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(HttpMessageNotReadableException e, HttpServletRequest request) throws IOException {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        String message = this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid input format", new Object[0]);
        Throwable cause = e.getCause();
        InvalidInputResponse invalidInputResponse;
        switch (cause) {
            case InvalidFormatException invalidFormatException -> {
                String fieldPath = (String) invalidFormatException.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
                if (invalidFormatException.getTargetType() != null && invalidFormatException.getTargetType().isEnum()) {
                    message = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.", invalidFormatException.getValue(), fieldPath, Arrays.toString(invalidFormatException.getTargetType().getEnumConstants()));
                }

                invalidInputResponse = new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.name(), Collections.singleton(FieldErrorResponse.builder().field(fieldPath).message(message).build()));
            }
            case JsonParseException jsonParseException ->
                    invalidInputResponse = new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.name(), Collections.singleton(FieldErrorResponse.builder().field(jsonParseException.getProcessor().getCurrentName()).message(message).build()));
            case MismatchedInputException mismatchedInputException ->
                    invalidInputResponse = new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.getName(), Collections.singleton(FieldErrorResponse.builder().field((String) mismatchedInputException.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."))).message("Mismatched input").build()));
            case JsonMappingException jsonMappingException ->
                    invalidInputResponse = new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.getName(), Collections.singleton(FieldErrorResponse.builder().field((String) jsonMappingException.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."))).message("Json mapping invalid").build()));
            case null, default ->
                    invalidInputResponse = new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.getName(), Collections.singleton(FieldErrorResponse.builder().message(message).build()));
        }

        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidInputResponse);
    }

    @ExceptionHandler({ResponseException.class})
    public ResponseEntity<ErrorResponse<Object>> handleResponseException(ResponseException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getError().getMessage());
        ResponseError error = e.getError();
        String message = this.getMessage(error.getName(), e.getError().getMessage(), e.getParams());
        this.catchException(e);
        return ResponseEntity.status(error.getStatus()).body(ErrorResponse.builder().code(error.getCode()).error(error.getName()).data(e.getParams()).message(message).build());
    }

    @ExceptionHandler({InvocationTargetException.class})
    public ResponseEntity<ErrorResponse<?>> handleResponseException(InvocationTargetException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error("Failed to handle request {}: {}", request.getRequestURI(), error.getMessage());
        String msg = this.getMessage(InternalServerError.INTERNAL_SERVER_ERROR.getName(), "There are somethings wrong: {0}", new Object[]{e});
        this.catchException(e);
        return ResponseEntity.status(error.getStatus()).body(ErrorResponse.builder().code(error.getCode()).error(error.getName()).message(msg).build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        Set<?> fieldErrors = bindingResult.getAllErrors().stream().map((objectError) -> {
            try {
                FieldError fieldError = (FieldError) objectError;
                String message = this.exceptionTranslator.translate(fieldError.getDefaultMessage());
                return FieldErrorResponse.builder().field(fieldError.getField()).objectName(fieldError.getObjectName()).message(message).build();
            } catch (ClassCastException var4) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.getName(), (Set<FieldErrorResponse>) fieldErrors));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse<?>> handleResponseException(Exception e, HttpServletRequest request) {
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error("Failed to handle request {}: {}", request.getRequestURI(), error.getMessage());
        String msg = this.getMessage(InternalServerError.INTERNAL_SERVER_ERROR.getName(), "There are somethings wrong: {0}", new Object[]{e});
        this.catchException(e);
        return ResponseEntity.status(error.getStatus()).body(ErrorResponse.builder().code(error.getCode()).error(error.getName()).message(msg).build());
    }

    @ExceptionHandler({RetryableException.class})
    public ResponseEntity<ErrorResponse<?>> handleResponseException(RetryableException e, HttpServletRequest request) {
        ResponseError error = ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR;
        log.error("Failed to handle request {}: {}", request.getRequestURI(), error.getMessage());
        String msg = this.getMessage(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR.getName(), ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR.getMessage(), new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(error.getStatus()).body(ErrorResponse.builder().code(error.getCode()).error(error.getName()).message(msg).build());
    }

    @ExceptionHandler({NoFallbackAvailableException.class})
    public ResponseEntity<ErrorResponse<?>> handleResponseException(NoFallbackAvailableException e, HttpServletRequest request) {
        ResponseError error = ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR;
        log.error("Failed to handle request {}: {}", request.getRequestURI(), error.getMessage());
        String msg = this.getMessage(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR.getName(), ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR.getMessage(), new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(error.getStatus()).body(ErrorResponse.builder().code(error.getCode()).error(error.getName()).message(msg).build());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, NonTransientDataAccessException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponse<?>> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        ResponseError error = InternalServerError.DATA_ACCESS_EXCEPTION;
        log.error("Failed to handle request {}: {}", request.getRequestURI(), error.getMessage());
        log.error(e.getMessage(), e);
        String msg = this.getMessage(InternalServerError.DATA_ACCESS_EXCEPTION.getName(), "Data access exception", new Object[]{e.getClass().getName()});
        this.catchException(e);
        return ResponseEntity.status(error.getStatus()).body(ErrorResponse.builder().code(error.getCode()).error(error.getName()).message(msg).build());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        String message = this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), message, BadRequestError.INVALID_INPUT.getName(), Collections.singleton(FieldErrorResponse.builder().field(e.getParameterName()).message(e.getMessage()).build())));
    }

    @ExceptionHandler({MissingServletRequestPartException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(MissingServletRequestPartException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
        String message = this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), message, BadRequestError.INVALID_INPUT.getName()));
    }

    @ExceptionHandler({MultipartException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(MultipartException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
        String message = this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), message, BadRequestError.INVALID_INPUT.getName()));
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(BindException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        Set<?> fieldsErrors = e.getFieldErrors().stream().map((fieldError) -> FieldErrorResponse.builder().field(fieldError.getField()).objectName(fieldError.getObjectName()).build()).collect(Collectors.toSet());
        String message = this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]);
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), message, BadRequestError.INVALID_INPUT.name(), (Set<FieldErrorResponse>) fieldsErrors));
    }

    @ExceptionHandler({MismatchedInputException.class})
    public ResponseEntity<InvalidInputResponse> handleValidationException(MismatchedInputException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidInputResponse(HttpStatus.BAD_REQUEST.value(), this.getMessage(BadRequestError.INVALID_INPUT.getName(), "Invalid request arguments", new Object[0]), BadRequestError.INVALID_INPUT.getName(), Collections.singleton(FieldErrorResponse.builder().message(e.getMessage()).build())));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse<?>> handleValidationException(AccessDeniedException e, HttpServletRequest request) {
        String var10001 = request.getMethod();
        log.warn("Failed to handle request {}: {}", var10001, request.getRequestURI(), e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.builder().code(AuthorizationError.ACCESS_DENIED.getCode()).error(AuthorizationError.ACCESS_DENIED.getName()).message(this.getMessage(AuthorizationError.ACCESS_DENIED.getName(), "Access Denied", new Object[0])).build());
    }

    @ExceptionHandler({InsufficientAuthenticationException.class})
    public ResponseEntity<ErrorResponse<?>> handleValidationException(InsufficientAuthenticationException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        this.catchException(e);
        ResponseEntity.BodyBuilder var10000 = ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        ErrorResponse.ErrorResponseBuilder var10001 = ErrorResponse.builder().error(AuthenticationError.UNAUTHORISED.getName());
        String var10002 = request.getMethod();
        return var10000.body((ErrorResponse<Void>) var10001.message("You were not authorized to request " + var10002 + " " + request.getRequestURI()).build());
    }

    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorResponse<?>> handleValidationException(InternalAuthenticationServiceException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder().error(AuthenticationError.UNAUTHORISED.getName()).message(e.getMessage()).build());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponse<?>> handleValidationException(BadCredentialsException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder().error(AuthenticationError.UNAUTHORISED.getName()).message(e.getMessage()).build());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse<?>> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder().error(AuthenticationError.UNAUTHORISED.getName()).code(AuthenticationError.UNAUTHORISED.getCode()).message(this.getMessage(AuthorizationError.NOT_SUPPORTED_AUTHENTICATION.getName(), "Your authentication has not been supported yet", new Object[0])).build());
    }

    @ExceptionHandler({ForwardInnerAlertException.class})
    public ResponseEntity<ErrorResponse<?>> handleValidationException(ForwardInnerAlertException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().error(e.getResponse().getError()).message(e.getResponse().getMessage()).code(e.getResponse().getCode()).build());
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse<?>> handleMethodNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        String var10001 = request.getRequestURI();
        log.warn("Failed to handle request {}: {}", var10001, e.getMessage());
        this.catchException(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error(NotFoundError.PAGE_NOT_FOUND.getName()).code(NotFoundError.PAGE_NOT_FOUND.getCode()).message(this.getMessage(NotFoundError.PAGE_NOT_FOUND.getName(), NotFoundError.PAGE_NOT_FOUND.getMessage(), new Object[]{e.getResourcePath()})).build());
    }
}
