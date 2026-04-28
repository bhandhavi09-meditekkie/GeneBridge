package com.genebridge.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard API response wrapper for all endpoints
 */
public class ApiResponse {
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("data")
    private Object data;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("timestamp")
    private Long timestamp;
    
    // Default constructor
    public ApiResponse() {
    }
    
    // All-args constructor
    public ApiResponse(String status, Object data, String message, Long timestamp) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * Create a success response
     */
    public static ApiResponse success(Object data, String message) {
        return new ApiResponse("success", data, message, System.currentTimeMillis());
    }
    
    /**
     * Create an error response
     */
    public static ApiResponse error(String message) {
        return new ApiResponse("error", null, message, System.currentTimeMillis());
    }
    
    /**
     * Create a warning response
     */
    public static ApiResponse warning(Object data, String message) {
        return new ApiResponse("warning", data, message, System.currentTimeMillis());
    }
}