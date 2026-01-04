package com.moodsound.backend.service;

import com.moodsound.backend.model.FavoriteTrack;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.model.User;
import com.moodsound.backend.repository.FavoriteTrackRepository;
import com.moodsound.backend.repository.TrackRepository;
import com.moodsound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteTrackRepository favoriteTrackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrackRepository trackRepository;


    public List<Track> getFavoritesByUserId(Integer userId) {
        List<FavoriteTrack> favorites = favoriteTrackRepository.findByUserIdOrderByAddedAtDesc(userId);

        // Extraer solo las canciones
        List<Track> tracks = new ArrayList<>();
        for (FavoriteTrack favorite : favorites) {
            tracks.add(favorite.getTrack());
        }

        return tracks;
    }


    public void addFavorite(Integer userId, Integer trackId) {
        // Verificar si ya es favorito
        if (favoriteTrackRepository.existsByUserIdAndTrackId(userId, trackId)) {
            return; // Ya existe, no hacer nada
        }

        // Buscar usuario y canción
        User user = userRepository.findById(userId).orElse(null);
        Track track = trackRepository.findById(trackId).orElse(null);

        if (user != null && track != null) {
            FavoriteTrack favorite = new FavoriteTrack();
            favorite.setUser(user);
            favorite.setTrack(track);
            favoriteTrackRepository.save(favorite);
        }
    }


    public void removeFavorite(Integer userId, Integer trackId) {
        favoriteTrackRepository.deleteByUserIdAndTrackId(userId, trackId);
    }


    public boolean isFavorite(Integer userId, Integer trackId) {
        return favoriteTrackRepository.existsByUserIdAndTrackId(userId, trackId);
    }
}