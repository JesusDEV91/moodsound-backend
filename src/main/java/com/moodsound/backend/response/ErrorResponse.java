package com.moodsound.backend.response;

public class ErrorResponse {
    private String error;

    // Constructor vacío
    public ErrorResponse() {
    }

    // Constructor con error
    public ErrorResponse(String error) {
        this.error = error;
    }

    // Getter y Setter
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}