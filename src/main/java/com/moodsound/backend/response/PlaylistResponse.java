package com.moodsound.backend.response;

import com.moodsound.backend.model.Track;
import java.util.List;

public class PlaylistResponse {
    private String mood;
    private String displayName;
    private String emoji;
    private String color;
    private List<Track> tracks;
    private List<TrackWithFavoriteResponse> tracksWithFavorite;
    private boolean authenticated;

    public PlaylistResponse() {}

    // Getters y Setters
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<Track> getTracks() { return tracks; }
    public void setTracks(List<Track> tracks) { this.tracks = tracks; }

    public List<TrackWithFavoriteResponse> getTracksWithFavorite() { return tracksWithFavorite; }
    public void setTracksWithFavorite(List<TrackWithFavoriteResponse> tracksWithFavorite) {
        this.tracksWithFavorite = tracksWithFavorite;
    }

    public boolean isAuthenticated() { return authenticated; }
    public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }
}