package com.moodsound.backend.response;

public class MoodHistoryItem {
    private Integer id;
    private MoodInfo mood;
    private String inputText;
    private String detectedBy;
    private String createdAt;

    // Constructor vacío
    public MoodHistoryItem() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MoodInfo getMood() {
        return mood;
    }

    public void setMood(MoodInfo mood) {
        this.mood = mood;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Clase interna para info del mood
    public static class MoodInfo {
        private String name;
        private String displayName;
        private String emoji;

        public MoodInfo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getEmoji() {
            return emoji;
        }

        public void setEmoji(String emoji) {
            this.emoji = emoji;
        }
    }
}