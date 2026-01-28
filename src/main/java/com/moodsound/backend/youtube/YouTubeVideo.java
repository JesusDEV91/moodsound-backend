package com.moodsound.backend.youtube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase que representa un video de YouTube obtenido de la API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeVideo {

    private VideoId id;
    private VideoSnippet snippet;

    public YouTubeVideo() {
    }

    // Métodos helper para acceso fácil
    public String getVideoId() {
        return id != null ? id.getVideoId() : null;
    }

    public String getTitle() {
        return snippet != null ? snippet.getTitle() : null;
    }

    public String getChannelTitle() {
        return snippet != null ? snippet.getChannelTitle() : null;
    }

    public String getThumbnailUrl() {
        if (snippet != null && snippet.getThumbnails() != null &&
                snippet.getThumbnails().getHigh() != null) {
            return snippet.getThumbnails().getHigh().getUrl();
        }
        return null;
    }

    // Getters y Setters principales
    public VideoId getId() {
        return id;
    }

    public void setId(VideoId id) {
        this.id = id;
    }

    public VideoSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(VideoSnippet snippet) {
        this.snippet = snippet;
    }

    // Clases internas
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VideoId {
        @JsonProperty("videoId")
        private String videoId;

        public VideoId() {
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VideoSnippet {
        private String title;

        @JsonProperty("channelTitle")
        private String channelTitle;

        private Thumbnails thumbnails;

        public VideoSnippet() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnails {
        private Thumbnail high;

        public Thumbnails() {
        }

        public Thumbnail getHigh() {
            return high;
        }

        public void setHigh(Thumbnail high) {
            this.high = high;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnail {
        private String url;
        private Integer width;
        private Integer height;

        public Thumbnail() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }
}