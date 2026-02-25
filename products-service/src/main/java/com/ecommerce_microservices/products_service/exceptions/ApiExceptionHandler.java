package com.ecommerce_microservices.products_service.exceptions;

import com.ecommerce_microservices.products_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex) {
        ApiError error = new ApiError(ex.getMessage(), "BAD_REQUEST");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("Bad Request", List.of(error)));
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(NotFoundException ex) {
        ApiError error = new ApiError(ex.getMessage(), "NOT_FOUND");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure("Not Found", List.of(error)));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflictException(ConflictException ex) {
        ApiError error = new ApiError(ex.getMessage(), "CONFLICT");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure("Conflict", List.of(error)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneral(Exception ex) {
        ApiError error = new ApiError( "Internal Server Error", "INTERNAL_SERVER_ERROR");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure("Internal Server Error", List.of(error)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ApiError> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errors.add(new ApiError(
                    fieldError.getField() + ": " + fieldError.getDefaultMessage(), "BAD_REQUEST"));
        });


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("Bad Request", errors));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStockException(InsufficientStockException ex) {
        ApiError error = new ApiError(ex.getMessage(), "CONFLICT");

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure("Conflict", List.of(error)));
    }

}
