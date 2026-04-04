package com.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String  message;
    private T       data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    // Success response with custom message and data
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // Optional: Failure response helper
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }

}
