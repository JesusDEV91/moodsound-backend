package com.moodsound.backend.service;

import com.moodsound.backend.youtube.YouTubeSearchResponse;
import com.moodsound.backend.youtube.YouTubeVideo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class YouTubeService {

    @Value("${youtube.api-key}")
    private String apiKey;

    @Value("${youtube.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Busca videos musicales en YouTube
     */
    public List<YouTubeVideo> searchVideos(String query, int maxResults) {

        // Construir URL de búsqueda
        String searchUrl = apiUrl + "/search" +
                "?part=snippet" +
                "&type=video" +
                "&videoCategoryId=10" +  // Categoría Música
                "&q=" + query +
                "&maxResults=" + maxResults +
                "&key=" + apiKey;

        try {
            // Llamar a YouTube Data API
            ResponseEntity<YouTubeSearchResponse> response = restTemplate.getForEntity(
                    searchUrl,
                    YouTubeSearchResponse.class
            );

            if (response.getBody() != null && response.getBody().getItems() != null) {
                return response.getBody().getItems();
            }

        } catch (Exception e) {
            System.err.println("Error al buscar en YouTube: " + e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Busca música por mood específico
     */
    public List<YouTubeVideo> searchByMood(String moodName, int maxResults) {
        String query = "";

        switch (moodName.toLowerCase()) {
            case "happy":
                query = "happy upbeat music";
                break;
            case "sad":
                query = "sad emotional music";
                break;
            case "energetic":
                query = "energetic workout music";
                break;
            case "chill":
                query = "chill relaxing music";
                break;
            default:
                query = moodName + " music";
        }

        return searchVideos(query, maxResults);
    }
}