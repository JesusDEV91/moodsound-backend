package com.moodsound.backend.response;

public class MessageResponse {
    private String message;

    // Constructor vacío
    public MessageResponse() {
    }

    // Constructor con mensaje
    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter y Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}