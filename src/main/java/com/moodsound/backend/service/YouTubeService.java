package com.moodsound.backend.service;

import com.moodsound.backend.youtube.YouTubeSearchResponse;
import com.moodsound.backend.youtube.YouTubeVideo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class YouTubeService {

    @Value("${youtube.api-key}")
    private String apiKey;

    @Value("${youtube.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();


    public List<YouTubeVideo> searchVideos(String query, int maxResults) {

        String[] orders = {"relevance", "date", "viewCount", "rating"};
        String order = orders[random.nextInt(orders.length)];

        String searchUrl = apiUrl + "/search" +
                "?part=snippet" +
                "&type=video" +
                "&videoCategoryId=10" +
                "&order=" + order +
                "&q=" + query +
                "&maxResults=" + maxResults +
                "&key=" + apiKey;

        try {
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


    public List<YouTubeVideo> searchByMood(String moodName, int maxResults) {
        String query = getRandomQueryForMood(moodName);
        return searchVideos(query, maxResults);
    }

    private String getRandomQueryForMood(String moodName) {
        String[] queries;

        switch (moodName.toLowerCase()) {
            case "happy":
                queries = new String[]{
                        "musica alegre",
                        "canciones felices",
                        "musica positiva",
                        "canciones animadas",
                        "musica de buen humor",
                        "musica para bailar feliz"
                };
                break;

            case "sad":
                queries = new String[]{
                        "musica triste",
                        "canciones melancolicas",
                        "musica romantica triste",
                        "baladas emotivas",
                        "musica para llorar",
                        "canciones de desamor"
                };
                break;

            case "energetic":
                queries = new String[]{
                        "musica energetica",
                        "musica para entrenar",
                        "canciones motivadoras",
                        "musica de gimnasio",
                        "musica para correr",
                        "musica intensa"
                };
                break;

            case "chill":
                queries = new String[]{
                        "musica relajante",
                        "musica tranquila",
                        "lofi en español",
                        "musica ambiente",
                        "musica para estudiar",
                        "musica chill"
                };
                break;

            default:
                return moodName + " music";
        }

        int index = random.nextInt(queries.length);
        return queries[index];
    }
}