package com.example.plans.exception;

import com.example.plans.dto.AppResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));

        AppResponseDto response = new AppResponseDto(4001, errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle IllegalArgumentException from service layer
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Illegal argument: {}", ex.getMessage());
        AppResponseDto response = new AppResponseDto(4002, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle DuplicateKeyException if not wrapped
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<AppResponseDto> handleDuplicateKeyException(DuplicateKeyException ex) {
        logger.error("Duplicate key: {}", ex.getMessage());
        AppResponseDto response = new AppResponseDto(4003, "Duplicate key error", null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handle custom ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppResponseDto> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.error("Resource not found: {}", ex.getMessage());
        AppResponseDto response = new AppResponseDto(4004, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponseDto> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        AppResponseDto response = new AppResponseDto(5000, "Internal Server Error", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
