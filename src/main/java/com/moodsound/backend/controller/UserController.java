package com.moodsound.backend.controller;

import com.moodsound.backend.model.MoodEntry;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.response.*;
import com.moodsound.backend.service.FavoriteService;
import com.moodsound.backend.service.MoodEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private MoodEntryService moodEntryService;

    /**
     * GET /api/user/{userId}/favorites
     * Obtiene las canciones favoritas de un usuario
     */
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<Track>> getFavorites(@PathVariable Integer userId) {
        List<Track> tracks = favoriteService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(tracks);
    }

    /**
     * POST /api/user/{userId}/favorites/{trackId}
     * Añade una canción a favoritos
     */
    @PostMapping("/{userId}/favorites/{trackId}")
    public ResponseEntity<MessageResponse> addFavorite(
            @PathVariable Integer userId,
            @PathVariable Integer trackId) {

        favoriteService.addFavorite(userId, trackId);

        MessageResponse response = new MessageResponse("Canción añadida a favoritos");
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/user/{userId}/favorites/{trackId}
     * Elimina una canción de favoritos
     */
    @DeleteMapping("/{userId}/favorites/{trackId}")
    public ResponseEntity<MessageResponse> removeFavorite(
            @PathVariable Integer userId,
            @PathVariable Integer trackId) {

        favoriteService.removeFavorite(userId, trackId);

        MessageResponse response = new MessageResponse("Canción eliminada de favoritos");
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/user/{userId}/favorites/{trackId}/check
     * Verifica si una canción es favorita
     */
    @GetMapping("/{userId}/favorites/{trackId}/check")
    public ResponseEntity<FavoriteCheckResponse> checkFavorite(
            @PathVariable Integer userId,
            @PathVariable Integer trackId) {

        boolean isFavorite = favoriteService.isFavorite(userId, trackId);

        FavoriteCheckResponse response = new FavoriteCheckResponse(isFavorite);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/user/{userId}/history
     * Obtiene el historial de moods del usuario
     */
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<MoodHistoryItem>> getHistory(@PathVariable Integer userId) {

        List<MoodEntry> entries = moodEntryService.getHistoryByUserId(userId);

        // Convertir a objetos de respuesta
        List<MoodHistoryItem> history = new ArrayList<>();

        for (MoodEntry entry : entries) {
            MoodHistoryItem item = new MoodHistoryItem();
            item.setId(entry.getId());

            // Info del mood
            MoodHistoryItem.MoodInfo moodInfo = new MoodHistoryItem.MoodInfo();
            moodInfo.setName(entry.getMood().getName());
            moodInfo.setDisplayName(entry.getMood().getDisplayName());
            moodInfo.setEmoji(entry.getMood().getEmoji());
            item.setMood(moodInfo);

            item.setInputText(entry.getInputText());
            item.setDetectedBy(entry.getDetectedBy());
            item.setCreatedAt(entry.getCreatedAt().toString());

            history.add(item);
        }

        return ResponseEntity.ok(history);
    }

    /**
     * POST /api/user/{userId}/history
     * Guarda una entrada en el historial
     */
    @PostMapping("/{userId}/history")
    public ResponseEntity<MessageResponse> saveHistory(
            @PathVariable Integer userId,
            @RequestBody SaveHistoryRequest request) {

        moodEntryService.saveMoodEntry(
                userId,
                request.getMoodId(),
                request.getInputText(),
                request.getDetectedBy()
        );

        MessageResponse response = new MessageResponse("Entrada guardada en historial");
        return ResponseEntity.ok(response);
    }
}