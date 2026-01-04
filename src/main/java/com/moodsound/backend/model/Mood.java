package com.moodsound.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moods")
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(length = 10)
    private String emoji;

    @Column(length = 7)
    private String color;

    // Relaciones - IGNORAR en JSON
    @JsonIgnore
    @OneToMany(mappedBy = "mood", cascade = CascadeType.ALL)
    private List<MoodTrack> moodTracks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "mood", cascade = CascadeType.ALL)
    private List<MoodEntry> moodEntries = new ArrayList<>();

    // CONSTRUCTORES
    public Mood() {
    }

    // GETTERS Y SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<MoodTrack> getMoodTracks() {
        return moodTracks;
    }

    public void setMoodTracks(List<MoodTrack> moodTracks) {
        this.moodTracks = moodTracks;
    }

    public List<MoodEntry> getMoodEntries() {
        return moodEntries;
    }

    public void setMoodEntries(List<MoodEntry> moodEntries) {
        this.moodEntries = moodEntries;
    }
}