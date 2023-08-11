package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(ValidationException e) {
        log.warn(e.getMessage());
        return formError(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(MissingServletRequestParameterException e) {
        log.warn(e.getMessage());
        return formError(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handle(Exception e) {
        log.warn(e.getMessage());
        return formError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ApiError formError(HttpStatus status, Throwable exception) {
        return new ApiError(status.toString(),
                exception.getMessage(),
                exception.getCause(),
                LocalDateTime.now());
    }
}
