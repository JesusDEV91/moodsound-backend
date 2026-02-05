package com.moodsound.backend.controller;

import com.moodsound.backend.model.AudienceType;
import com.moodsound.backend.model.Mood;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.response.ErrorResponse;
import com.moodsound.backend.response.PlaylistResponse;
import com.moodsound.backend.service.MoodService;
import com.moodsound.backend.service.YouTubeService;
import com.moodsound.backend.service.TrackService;
import com.moodsound.backend.youtube.YouTubeVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{moodName}")
    public ResponseEntity<?> getPlaylist(
            @PathVariable String moodName,
            @RequestParam(defaultValue = "ADULT") String audience) {

        try {

            AudienceType audienceEnum = AudienceType.valueOf(audience.toUpperCase());

            Mood mood = moodService.getMoodByName(moodName);

            if (mood == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Mood no encontrado: " + moodName));
            }

            // Buscamos canciones filtrando por mood y audiencia
            List<Track> tracks = trackService.getTracksByMoodAndAudience(moodName, audienceEnum);

            if (tracks.isEmpty()) {
                tracks = cargarCancionesDesdeYouTube(mood, moodName, audienceEnum);
            }

            PlaylistResponse response = new PlaylistResponse();
            response.setMood(mood.getName());
            response.setDisplayName(mood.getDisplayName());
            response.setEmoji(mood.getEmoji());
            response.setColor(mood.getColor());
            response.setTracks(tracks);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // Esto hará que veas el error real en la consola de IntelliJ
            return ResponseEntity.status(500).body(new ErrorResponse("Error interno del servidor: " + e.getMessage()));
        }
    }

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

            trackService.removeAllTracksFromMood(mood.getId());
            List<Track> tracks = cargarCancionesDesdeYouTube(mood, moodName, audienceEnum);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Playlist actualizada con " + tracks.size() + " canciones");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error al refrescar: " + e.getMessage()));
        }
    }

    private List<Track> cargarCancionesDesdeYouTube(Mood mood, String moodName, AudienceType audience) {

        String queryBusqueda;
        if (audience == AudienceType.KIDS) {

            queryBusqueda = moodName + " canciones infantiles para niños kids nursery rhymes";
        } else {

            queryBusqueda = moodName + " official music song -kids -children -nursery -infantil";
        }


        List<YouTubeVideo> youtubeVideos = youtubeService.searchByMood(queryBusqueda, 15);

        int position = 1;
        for (YouTubeVideo video : youtubeVideos) {

            Track track = new Track();
            track.setYoutubeId(video.getVideoId());
            track.setTitle(video.getTitle());
            track.setArtist(video.getChannelTitle());
            track.setThumbnailUrl(video.getThumbnailUrl());
            track.setExternalUrl("https://www.youtube.com/watch?v=" + video.getVideoId());


            track.setAudienceType(audience);


            Track savedTrack = trackService.saveTrack(track);
            if (savedTrack.getAudienceType() != audience) {
                savedTrack.setAudienceType(audience);
                trackService.saveTrack(savedTrack);
            }


            trackService.addTrackToMood(mood.getId(), savedTrack.getId(), position);
            position++;
        }
        
        return trackService.getTracksByMoodAndAudience(moodName, audience);
    }
}