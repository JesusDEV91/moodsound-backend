package com.moodsound.backend.controller;

import com.moodsound.backend.model.Mood;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.response.CountResponse;
import com.moodsound.backend.response.ErrorResponse;
import com.moodsound.backend.response.MessageResponse;
import com.moodsound.backend.response.PlaylistResponse;
import com.moodsound.backend.service.MoodService;
import com.moodsound.backend.service.YouTubeService;
import com.moodsound.backend.service.TrackService;
import com.moodsound.backend.youtube.YouTubeVideo;
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
    private YouTubeService youtubeService;

    /**
     * GET /api/playlist/{moodName}
     * Obtiene la playlist de canciones para un mood específico
     */
    @GetMapping("/{moodName}")
    public ResponseEntity<?> getPlaylist(@PathVariable String moodName) {

        // Buscar mood en BD
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

    /**
     * POST /api/playlist/{moodName}/refresh
     * Actualiza la playlist llamando a YouTube API
     */
    @PostMapping("/{moodName}/refresh")
    public ResponseEntity<?> refreshPlaylist(@PathVariable String moodName) {

        // Buscar mood en BD
        Mood mood = moodService.getMoodByName(moodName);

        if (mood == null) {
            ErrorResponse error = new ErrorResponse("Mood no encontrado: " + moodName);
            return ResponseEntity.badRequest().body(error);
        }

        // Eliminar canciones antiguas del mood
        trackService.removeAllTracksFromMood(mood.getId());

        // Buscar nuevas canciones en YouTube
        List<YouTubeVideo> youtubeVideos = youtubeService.searchByMood(moodName, 15);

        int position = 1;
        for (YouTubeVideo video : youtubeVideos) {

            // Crear entidad Track desde YouTubeVideo
            Track track = new Track();
            track.setYoutubeId(video.getVideoId());
            track.setTitle(video.getTitle());
            track.setArtist(video.getChannelTitle());
            track.setAlbum(null);  // YouTube no tiene álbum
            track.setThumbnailUrl(video.getThumbnailUrl());
            track.setExternalUrl("https://music.youtube.com/watch?v=" + video.getVideoId());
            track.setDurationMs(null);  // Opcional: YouTube no devuelve duración en search
            track.setPopularity(null);  // Opcional: YouTube no tiene popularity

            // Guardar track en BD
            Track savedTrack = trackService.saveTrack(track);

            // Asociar track con mood
            trackService.addTrackToMood(mood.getId(), savedTrack.getId(), position);
            position++;
        }

        MessageResponse response = new MessageResponse(
                "Playlist actualizada con " + youtubeVideos.size() + " canciones de YouTube Music"
        );

        return ResponseEntity.ok(response);
    }
}