package com.moodsound.backend.controller;

import com.moodsound.backend.model.Mood;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.response.CountResponse;
import com.moodsound.backend.response.ErrorResponse;
import com.moodsound.backend.response.MessageResponse;
import com.moodsound.backend.response.PlaylistResponse;
import com.moodsound.backend.service.MoodService;
import com.moodsound.backend.service.SpotifyService;
import com.moodsound.backend.service.TrackService;
import com.moodsound.backend.spotify.SpotifyTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
@CrossOrigin(origins = "*")
public class PlaylistController {

    @Autowired
    private TrackService trackService;

    @Autowired
    private MoodService moodService;
    @Autowired
    private SpotifyService spotifyService;


    @GetMapping("/{moodName}")
    public ResponseEntity<?> getPlaylist(@PathVariable String moodName) {


        Mood mood = moodService.getMoodByName(moodName);

        if (mood == null) {
            ErrorResponse error = new ErrorResponse("Mood no encontrado: " + moodName);
            return ResponseEntity.badRequest().body(error);
        }

        // Obtener canciones del mood
        List<Track> tracks = trackService.getTracksByMoodName(moodName);

        // Crear respuesta
        PlaylistResponse response = new PlaylistResponse();
        response.setMood(mood.getName());
        response.setDisplayName(mood.getDisplayName());
        response.setEmoji(mood.getEmoji());
        response.setColor(mood.getColor());
        response.setTracks(tracks);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/playlist/count/{moodName}
     * Cuenta cuántas canciones tiene un mood
     */
    @GetMapping("/count/{moodName}")
    public ResponseEntity<?> countTracks(@PathVariable String moodName) {

        Mood mood = moodService.getMoodByName(moodName);

        if (mood == null) {
            ErrorResponse error = new ErrorResponse("Mood no encontrado");
            return ResponseEntity.badRequest().body(error);
        }

        int count = trackService.countTracksByMood(mood.getId());

        CountResponse response = new CountResponse(moodName, count);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{moodName}/refresh")
    public ResponseEntity<?> refreshPlaylist(@PathVariable String moodName) {

        // Buscar el mood
        Mood mood = moodService.getMoodByName(moodName);

        if (mood == null) {
            ErrorResponse error = new ErrorResponse("Mood no encontrado: " + moodName);
            return ResponseEntity.badRequest().body(error);
        }

        // Eliminar canciones antiguas del mood
        trackService.removeAllTracksFromMood(mood.getId());

        // Buscar canciones en Spotify
        List<SpotifyTrack> spotifyTracks = spotifyService.searchByMood(moodName, 15);

        // Guardar cada canción en la BD
        int position = 1;
        for (SpotifyTrack spotifyTrack : spotifyTracks) {
            // Crear entidad Track
            Track track = new Track();
            track.setSpotifyId(spotifyTrack.getId());
            track.setTitle(spotifyTrack.getName());
            track.setArtist(spotifyTrack.getFirstArtistName());
            track.setAlbum(spotifyTrack.getAlbum().getName());
            track.setPreviewUrl(spotifyTrack.getPreviewUrl());
            track.setExternalUrl(spotifyTrack.getSpotifyUrl());
            track.setImageUrl(spotifyTrack.getAlbumImageUrl());
            track.setDurationMs(spotifyTrack.getDurationMs());
            track.setPopularity(spotifyTrack.getPopularity());

            // Guardar track (si no existe)
            Track savedTrack = trackService.saveTrack(track);

            // Asociar al mood
            trackService.addTrackToMood(mood.getId(), savedTrack.getId(), position);
            position++;
        }

        MessageResponse response = new MessageResponse(
                "Playlist actualizada con " + spotifyTracks.size() + " canciones de Spotify"
        );

        return ResponseEntity.ok(response);
    }
}