package com.moodsound.backend.youtube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Clase que representa la respuesta completa de búsqueda de YouTube
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeSearchResponse {

    private List<YouTubeVideo> items;

    public YouTubeSearchResponse() {
    }

    public List<YouTubeVideo> getItems() {
        return items;
    }

    public void setItems(List<YouTubeVideo> items) {
        this.items = items;
    }
}