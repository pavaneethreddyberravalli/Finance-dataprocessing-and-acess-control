package com.finance.excption;

import com.finance.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceAlreadyExitsException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceExists(ResourceAlreadyExitsException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationFailed(AuthenticationFailedException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ApiResponse<?>> handleTokenRefreshException(TokenRefreshException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({TokenNotFoundException.class, TokenExpiredException.class})
    public ResponseEntity<ApiResponse<?>> handleTokenExceptions(RuntimeException ex) {
        return new ResponseEntity<>(
                new ApiResponse<>(false, ex.getMessage(), null),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleTransactionNotFound(TransactionNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiResponse<>(false, ex.getMessage(), null),
                HttpStatus.NOT_FOUND
        );

    }
}