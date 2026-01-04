package com.moodsound.backend.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyTrack {

    private String id;
    private String name;
    private Album album;
    private List<Artist> artists;

    @JsonProperty("duration_ms")
    private Integer durationMs;

    private Integer popularity;

    @JsonProperty("preview_url")
    private String previewUrl;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    // Constructor vacío
    public SpotifyTrack() {
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    // Método helper para obtener el primer artista
    public String getFirstArtistName() {
        if (artists != null && !artists.isEmpty()) {
            return artists.get(0).getName();
        }
        return "Unknown Artist";
    }

    // Método helper para obtener la URL de Spotify
    public String getSpotifyUrl() {
        if (externalUrls != null) {
            return externalUrls.getSpotify();
        }
        return null;
    }

    // Método helper para obtener la imagen del álbum
    public String getAlbumImageUrl() {
        if (album != null && album.getImages() != null && !album.getImages().isEmpty()) {
            return album.getImages().get(0).getUrl();
        }
        return null;
    }

    // Clases internas
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Album {
        private String name;
        private List<Image> images;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Artist {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String url;
        private Integer height;
        private Integer width;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExternalUrls {
        private String spotify;

        public String getSpotify() {
            return spotify;
        }

        public void setSpotify(String spotify) {
            this.spotify = spotify;
        }
    }
}