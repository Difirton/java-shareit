package ru.practicum.shareit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.user.error.UserEmailAlreadyExistException;
import ru.practicum.shareit.user.error.UserNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String PATH = "path";
    private static final String REASONS = "reasons";

    @ExceptionHandler(value = {EmptyResultDataAccessException.class, UserNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = this.getErrorBody(HttpStatus.NOT_FOUND, request);
        body.put(REASONS, ex.getMessage());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class, UserEmailAlreadyExistException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = getErrorBody(HttpStatus.CONFLICT, request);
        Throwable cause = ex.getCause();
        if (cause != null) {
            body.put(REASONS, cause.getMessage());
        }
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = getErrorBody(HttpStatus.BAD_REQUEST, request);
        List<String> errors = Arrays.stream(ex.getMessage().split(", ")).collect(Collectors.toList());
        body.put(REASONS, errors);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private Map<String, Object> getErrorBody(HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, OffsetDateTime.now());
        body.put(STATUS, status.value());
        body.put(ERROR, status.getReasonPhrase());
        body.put(PATH, this.getRequestUri(request));
        return body;
    }

    private String getRequestUri(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();
            return String.format("%s %s", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        } else {
            return "";
        }
    }
}
