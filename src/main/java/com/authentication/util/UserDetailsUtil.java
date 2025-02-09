package com.authentication.util;

import com.authentication.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserDetailsUtil {

    private static ObjectMapper objectMapper;

    public UserDetailsUtil() {}

    public static <T> T convertObjectToPojo(Object data, Class<T> type) {
        return (T) objectMapper.convertValue(data, type);
    }

    public static <T> ResponseEntity<ApiResponse<T>> successObject(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setApiStatus("SUCCESS");
        response.setApiVersion("1.0");
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> successObject(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setApiStatus("SUCCESS");
        response.setApiVersion("1.0");
        response.setMessage(message);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ApiResponse<Object> errorObject(String message, String errorCode) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setApiStatus("FAILURE");
        response.setApiVersion("1.0");
        response.setErrorCode(errorCode);
        response.setMessage(message);
        return response;
    }

}
