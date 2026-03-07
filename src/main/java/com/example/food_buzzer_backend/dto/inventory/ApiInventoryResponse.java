package com.example.food_buzzer_backend.dto.inventory;

/**
 * Generic API Response wrapper for inventory endpoints
 * Used to wrap success/failure responses with messages and data
 */
public class ApiInventoryResponse {

    private Boolean success;
    private String message;
    private Object data;

    public ApiInventoryResponse() {}

    // Constructor with success and message
    public ApiInventoryResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }

    // Constructor with success, message, and data
    public ApiInventoryResponse(Boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getter and Setter for success
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    // Getter and Setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and Setter for data
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
