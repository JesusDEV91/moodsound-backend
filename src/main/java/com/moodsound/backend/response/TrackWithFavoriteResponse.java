package com.moodsound.backend.response;

import com.moodsound.backend.model.Track;


public class TrackWithFavoriteResponse {
    private Integer id;
    private String youtubeId;
    private String title;
    private String artist;
    private String album;
    private String thumbnailUrl;
    private String externalUrl;
    private Integer durationMs;
    private Integer popularity;
    private boolean isFavorite;

    public TrackWithFavoriteResponse() {}

    public TrackWithFavoriteResponse(Track track, boolean isFavorite) {
        this.id = track.getId();
        this.youtubeId = track.getYoutubeId();
        this.title = track.getTitle();
        this.artist = track.getArtist();
        this.album = track.getAlbum();
        this.thumbnailUrl = track.getThumbnailUrl();
        this.externalUrl = track.getExternalUrl();
        this.durationMs = track.getDurationMs();
        this.popularity = track.getPopularity();
        this.isFavorite = isFavorite;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getYoutubeId() { return youtubeId; }
    public void setYoutubeId(String youtubeId) { this.youtubeId = youtubeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getExternalUrl() { return externalUrl; }
    public void setExternalUrl(String externalUrl) { this.externalUrl = externalUrl; }

    public Integer getDurationMs() { return durationMs; }
    public void setDurationMs(Integer durationMs) { this.durationMs = durationMs; }

    public Integer getPopularity() { return popularity; }
    public void setPopularity(Integer popularity) { this.popularity = popularity; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}