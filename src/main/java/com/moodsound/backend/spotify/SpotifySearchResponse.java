package com.moodsound.backend.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifySearchResponse {

    private Tracks tracks;


    public SpotifySearchResponse() {
    }


    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    // Clase interna
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tracks {
        private List<SpotifyTrack> items;

        public List<SpotifyTrack> getItems() {
            return items;
        }

        public void setItems(List<SpotifyTrack> items) {
            this.items = items;
        }
    }
}