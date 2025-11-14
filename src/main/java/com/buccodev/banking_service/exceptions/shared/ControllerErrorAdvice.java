package com.buccodev.banking_service.exceptions.shared;

import com.buccodev.banking_service.exceptions.account.AccountAlreadyException;
import com.buccodev.banking_service.exceptions.card.CardAlreadyException;
import com.buccodev.banking_service.exceptions.card.CardLimitsException;
import com.buccodev.banking_service.exceptions.card.CardNotFoundException;
import com.buccodev.banking_service.exceptions.customer.CustomerAlreadyRegisteredException;
import com.buccodev.banking_service.exceptions.customer.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ControllerErrorAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                         HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StandardError error = new StandardError(timestamp, HttpStatus.NOT_FOUND.value(),
                request.getRequestURI(), List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccountAlreadyException.class)
    public ResponseEntity<StandardError> handleAccountAlreadyException(AccountAlreadyException ex,
                                                                        HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StandardError error = new StandardError(timestamp, HttpStatus.CONFLICT.value(),
                request.getRequestURI(), List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CardAlreadyException.class)
    public ResponseEntity<StandardError> handleCardAlreadyException(CardAlreadyException ex,
                                                                    HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StandardError error = new StandardError(timestamp, HttpStatus.CONFLICT.value(),
                request.getRequestURI(), List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CardLimitsException.class)
    public ResponseEntity<StandardError> handleCardLimitsException(CardLimitsException ex,
                                                                    HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StandardError error = new StandardError(timestamp, HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI(), List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<StandardError> handleCardNotFoundException(CardNotFoundException ex,
                                                                        HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StandardError error = new StandardError(timestamp, HttpStatus.NOT_FOUND.value(),
                request.getRequestURI(), List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CustomerAlreadyRegisteredException.class)
    public ResponseEntity<StandardError> handleCustomerAlreadyRegisteredException(CustomerAlreadyRegisteredException ex,
                                                                                  HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StandardError error = new StandardError(timestamp, HttpStatus.CONFLICT.value(),
                request.getRequestURI(), List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                                HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage()
                ))
                .toList();

        StandardError error = new StandardError(timestamp, HttpStatus.UNPROCESSABLE_ENTITY.value(),
                request.getRequestURI(), errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                    HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StandardError error = new StandardError(timestamp, HttpStatus.METHOD_NOT_ALLOWED.value(),
                request.getRequestURI(), List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                      HttpServletRequest request) {
        Throwable cause = ex.getCause();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (cause instanceof InvalidFormatException ife) {
            Class<?> targetType = ife.getTargetType();
            if (targetType.isEnum()) {
                Object[] constants = targetType.getEnumConstants();
                List<String> allowedValues = Arrays.stream(constants)
                        .map(Object::toString)
                        .toList();

                String allowed = String.join(", ", allowedValues);

                StandardError error = new StandardError(
                        timestamp,
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI(),
                        List.of(Map.of(
                                "error", "Invalid enum value",
                                "allowedValues", allowed
                        ))
                );
                return ResponseEntity.badRequest().body(error);
            }
        }

        StandardError error = new StandardError(
                timestamp,
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                List.of(Map.of("message", ex.getMessage()))
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleException(Exception ex, HttpServletRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StandardError error = new StandardError(timestamp, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI(), List.of(Map.of("message", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
