package com.moodsound.backend.service;

import com.moodsound.backend.model.Favorite;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.model.User;
import com.moodsound.backend.repository.FavoriteRepository;
import com.moodsound.backend.repository.TrackRepository;
import com.moodsound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrackRepository trackRepository;

    public List<Track> getFavoritesByUserId(Integer userId) {
        return favoriteRepository.findTracksByUserId(userId);
    }

    @Transactional
    public Favorite addFavorite(Integer userId, Integer trackId) {

        if (favoriteRepository.existsByUserIdAndTrackId(userId, trackId)) {
            throw new RuntimeException("Esta canción ya está en favoritos");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        Favorite favorite = new Favorite(user, track);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(Integer userId, Integer trackId) {
        if (!favoriteRepository.existsByUserIdAndTrackId(userId, trackId)) {
            throw new RuntimeException("Esta canción no está en favoritos");
        }

        favoriteRepository.deleteByUserIdAndTrackId(userId, trackId);
    }

    public boolean isFavorite(Integer userId, Integer trackId) {
        return favoriteRepository.existsByUserIdAndTrackId(userId, trackId);
    }

    public Long countFavorites(Integer userId) {
        return favoriteRepository.countByUserId(userId);
    }
}
