package com.moodsound.backend.response;

public class CountResponse {
    private String mood;
    private int count;

    // Constructor vacío
    public CountResponse() {
    }

    // Constructor completo
    public CountResponse(String mood, int count) {
        this.mood = mood;
        this.count = count;
    }

    // Getters y Setters
    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}