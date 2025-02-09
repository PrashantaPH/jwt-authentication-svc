package com.authentication.advoice;

import com.authentication.dto.ApiResponse;
import com.authentication.exception.UserAlreadyExistsException;
import com.authentication.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;

import static com.authentication.util.UserDetailsUtil.errorObject;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExists(UserAlreadyExistsException exception) {
        ApiResponse<Object> response = errorObject(exception.getMessage(), HttpStatus.CONFLICT.name());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserDetailsNotFound(UserNotFoundException exception) {
        ApiResponse<Object> response = errorObject(exception.getMessage(), HttpStatus.BAD_REQUEST.name());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidArgument(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldErrors().get(0);
        ApiResponse<Object> response = errorObject(fieldError.getDefaultMessage(), fieldError.getField());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleTechnicalException(Exception exception) {
        ApiResponse<Object> response = errorObject(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException exception) {
        ApiResponse<Object> response = errorObject(exception.getMessage(), HttpStatus.UNAUTHORIZED.name());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
