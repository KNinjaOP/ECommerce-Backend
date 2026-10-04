package com.krishna.ecommerce.exception;

import com.krishna.ecommerce.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException e) {

        ErrorResponse response = new ErrorResponse();

        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateResource(DuplicateResourceException e) {

        ErrorResponse response = new ErrorResponse();

        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse response = new ErrorResponse();

        response.setMessage(message);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidOrderStatus(
            InvalidOrderStatusException e) {

        ErrorResponse response = new ErrorResponse();

        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(InvalidPaymentStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidPaymentStatus(
            InvalidPaymentStatusException e) {

        ErrorResponse response = new ErrorResponse();

        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInsufficientStock(
            InsufficientStockException e) {

        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralException(Exception e) {

        ErrorResponse response = new ErrorResponse();

        response.setMessage("An unexpected error occurred");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(
            IllegalArgumentException e) {

        ErrorResponse response = new ErrorResponse();

        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentials(
            InvalidCredentialsException e) {

        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(
            DataIntegrityViolationException e) {

        ErrorResponse response = new ErrorResponse();
        response.setMessage(
                "Cannot delete this resource because it is being used by existing records"
        );
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setTimestamp(LocalDateTime.now());

        return response;
    }
}
