package com.moodsound.backend.controller;

import com.moodsound.backend.model.AudienceType;
import com.moodsound.backend.model.Mood;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.response.ErrorResponse;
import com.moodsound.backend.response.PlaylistResponse;
import com.moodsound.backend.response.TrackWithFavoriteResponse;
import com.moodsound.backend.service.FavoriteService;
import com.moodsound.backend.service.MoodService;
import com.moodsound.backend.service.YouTubeService;
import com.moodsound.backend.service.TrackService;
import com.moodsound.backend.service.UserService;
import com.moodsound.backend.youtube.YouTubeVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    /**
     * Obtiene una playlist según el mood y audiencia
     * Incluye información de favoritos si el usuario está autenticado
     * GET /api/playlist/{moodName}?audience=ADULT
     */
    @GetMapping("/{moodName}")
    public ResponseEntity<?> getPlaylist(
            @PathVariable String moodName,
            @RequestParam(defaultValue = "ADULT") String audience,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        try {
            AudienceType audienceEnum = AudienceType.valueOf(audience.toUpperCase());
            Mood mood = moodService.getMoodByName(moodName);

            if (mood == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Mood no encontrado: " + moodName));
            }

            List<Track> tracks = trackService.getTracksByMoodAndAudience(moodName, audienceEnum);

            // Si no hay tracks, buscar en YouTube
            if (tracks.isEmpty()) {
                String queryBusqueda;
                if (audienceEnum == AudienceType.KIDS) {
                    queryBusqueda = moodName + " canciones infantiles";
                } else {
                    queryBusqueda = moodName + " music";
                }

                try {
                    List<YouTubeVideo> youtubeVideos = youtubeService.searchByCustomQuery(queryBusqueda, 15);

                    int position = 1;
                    for (YouTubeVideo video : youtubeVideos) {
                        Track track = new Track();
                        track.setYoutubeId(video.getVideoId());
                        track.setTitle(video.getTitle());
                        track.setArtist(video.getChannelTitle());
                        track.setThumbnailUrl(video.getThumbnailUrl());
                        track.setExternalUrl("https://www.youtube.com/watch?v=" + video.getVideoId());
                        track.setAudienceType(audienceEnum);

                        Track savedTrack = trackService.saveTrack(track);
                        trackService.addTrackToMood(mood.getId(), savedTrack.getId(), position);
                        position++;
                    }

                    tracks = trackService.getTracksByMoodAndAudience(moodName, audienceEnum);

                } catch (Exception e) {
                    System.err.println("⚠️ YouTube API no disponible: " + e.getMessage());
                }
            }

            // Verificar si el usuario está autenticado
            Integer userId = getUserIdFromAuth(authHeader);

            // Si está autenticado, incluir información de favoritos
            if (userId != null) {
                List<TrackWithFavoriteResponse> tracksWithFavorite = new ArrayList<>();

                for (Track track : tracks) {
                    boolean isFavorite = favoriteService.isFavorite(userId, track.getId());
                    tracksWithFavorite.add(new TrackWithFavoriteResponse(track, isFavorite));
                }

                PlaylistResponse response = new PlaylistResponse();
                response.setMood(mood.getName());
                response.setDisplayName(mood.getDisplayName());
                response.setEmoji(mood.getEmoji());
                response.setColor(mood.getColor());
                response.setTracksWithFavorite(tracksWithFavorite);
                response.setAuthenticated(true);

                return ResponseEntity.ok(response);
            }

            // Usuario no autenticado - respuesta normal
            PlaylistResponse response = new PlaylistResponse();
            response.setMood(mood.getName());
            response.setDisplayName(mood.getDisplayName());
            response.setEmoji(mood.getEmoji());
            response.setColor(mood.getColor());
            response.setTracks(tracks);
            response.setAuthenticated(false);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Refresca una playlist con nuevas canciones de YouTube
     * POST /api/playlist/{moodName}/refresh?audience=ADULT
     */
    @PostMapping("/{moodName}/refresh")
    public ResponseEntity<?> refreshPlaylist(
            @PathVariable String moodName,
            @RequestParam(defaultValue = "ADULT") String audience) {

        try {
            AudienceType audienceEnum = AudienceType.valueOf(audience.toUpperCase());
            Mood mood = moodService.getMoodByName(moodName);

            if (mood == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Mood no encontrado: " + moodName));
            }

            String queryBusqueda;
            if (audienceEnum == AudienceType.KIDS) {
                queryBusqueda = moodName + " canciones infantiles";
            } else {
                queryBusqueda = moodName + " music";
            }

            List<YouTubeVideo> youtubeVideos;
            try {
                youtubeVideos = youtubeService.searchByCustomQuery(queryBusqueda, 15);
            } catch (Exception e) {
                return ResponseEntity.status(503).body(
                        new ErrorResponse("YouTube API no disponible. Se mantienen las canciones actuales.")
                );
            }

            if (youtubeVideos.isEmpty()) {
                return ResponseEntity.status(503).body(
                        new ErrorResponse("No se encontraron canciones en YouTube. Se mantienen las actuales.")
                );
            }

            trackService.removeAllTracksFromMood(mood.getId());

            int position = 1;
            for (YouTubeVideo video : youtubeVideos) {
                Track track = new Track();
                track.setYoutubeId(video.getVideoId());
                track.setTitle(video.getTitle());
                track.setArtist(video.getChannelTitle());
                track.setThumbnailUrl(video.getThumbnailUrl());
                track.setExternalUrl("https://www.youtube.com/watch?v=" + video.getVideoId());
                track.setAudienceType(audienceEnum);

                Track savedTrack = trackService.saveTrack(track);
                trackService.addTrackToMood(mood.getId(), savedTrack.getId(), position);
                position++;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Playlist actualizada con " + youtubeVideos.size() + " canciones");
            response.put("count", youtubeVideos.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("Error al refrescar: " + e.getMessage()));
        }
    }

    private Integer getUserIdFromAuth(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }

        try {
            String token = authHeader.replace("Bearer ", "");
            return userService.getUserIdFromToken(token);
        } catch (Exception e) {
            return null;
        }
    }
}