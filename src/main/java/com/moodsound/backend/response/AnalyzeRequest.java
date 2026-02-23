package com.moodsound.backend.response;

public class AnalyzeRequest {
    private String text;
    private String moodOption;


    public AnalyzeRequest() {
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMoodOption() {
        return moodOption;
    }

    public void setMoodOption(String moodOption) {
        this.moodOption = moodOption;
    }
}