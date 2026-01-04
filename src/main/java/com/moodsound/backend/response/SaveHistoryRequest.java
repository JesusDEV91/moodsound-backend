package com.moodsound.backend.response;

public class SaveHistoryRequest {
    private Integer moodId;
    private String inputText;
    private String detectedBy;

    // Constructor vacío
    public SaveHistoryRequest() {
    }

    // Getters y Setters
    public Integer getMoodId() {
        return moodId;
    }

    public void setMoodId(Integer moodId) {
        this.moodId = moodId;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getDetectedBy() {
        return detectedBy;
    }

    public void setDetectedBy(String detectedBy) {
        this.detectedBy = detectedBy;
    }
}