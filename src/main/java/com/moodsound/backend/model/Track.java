package com.moodsound.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tracks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "spotify_id", unique = true, nullable = false)
    private String spotifyId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private String album;

    @Column(name = "preview_url", columnDefinition = "TEXT")
    private String previewUrl;

    @Column(name = "external_url", nullable = false, columnDefinition = "TEXT")
    private String externalUrl;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "duration_ms")
    private Integer durationMs;

    private Integer popularity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relaciones - IGNORAR en JSON
    @JsonIgnore
    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private List<MoodTrack> moodTracks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private List<FavoriteTrack> favoriteTracks = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<MoodTrack> getMoodTracks() {
        return moodTracks;
    }

    public void setMoodTracks(List<MoodTrack> moodTracks) {
        this.moodTracks = moodTracks;
    }

    public List<FavoriteTrack> getFavoriteTracks() {
        return favoriteTracks;
    }

    public void setFavoriteTracks(List<FavoriteTrack> favoriteTracks) {
        this.favoriteTracks = favoriteTracks;
    }
}