package com.moodsound.backend.service;

import com.moodsound.backend.spotify.SpotifySearchResponse;
import com.moodsound.backend.spotify.SpotifyTokenResponse;
import com.moodsound.backend.spotify.SpotifyTrack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class SpotifyService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.api-url}")
    private String apiUrl;

    @Value("${spotify.auth-url}")
    private String authUrl;

    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();


    private void authenticate() {
        // Crear credenciales en Base64
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedCredentials);

        // Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        // Request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Llamar a Spotify
        ResponseEntity<SpotifyTokenResponse> response = restTemplate.postForEntity(
                authUrl,
                request,
                SpotifyTokenResponse.class
        );

        if (response.getBody() != null) {
            this.accessToken = response.getBody().getAccessToken();
        }
    }


    public List<SpotifyTrack> searchTracks(String query, int limit) {

        if (accessToken == null) {
            authenticate();
        }


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);


        String searchUrl = apiUrl + "/search?q=" + query + "&type=track&limit=" + limit;


        HttpEntity<String> request = new HttpEntity<>(headers);

        try {

            ResponseEntity<SpotifySearchResponse> response = restTemplate.exchange(
                    searchUrl,
                    HttpMethod.GET,
                    request,
                    SpotifySearchResponse.class
            );

            if (response.getBody() != null && response.getBody().getTracks() != null) {
                return response.getBody().getTracks().getItems();
            }

        } catch (Exception e) {

            authenticate();

            ResponseEntity<SpotifySearchResponse> response = restTemplate.exchange(
                    searchUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    SpotifySearchResponse.class
            );

            if (response.getBody() != null && response.getBody().getTracks() != null) {
                return response.getBody().getTracks().getItems();
            }
        }

        return new ArrayList<>();
    }

    public List<SpotifyTrack> searchByMood(String moodName, int limit) {
        String query = "";

        switch (moodName.toLowerCase()) {
            case "happy":
                query = "happy upbeat pop";
                break;
            case "sad":
                query = "sad emotional melancholic";
                break;
            case "energetic":
                query = "energetic workout pump";
                break;
            case "chill":
                query = "chill relaxing ambient";
                break;
            default:
                query = moodName;
        }

        return searchTracks(query, limit);
    }
}