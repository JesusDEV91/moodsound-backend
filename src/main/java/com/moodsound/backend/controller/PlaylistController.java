package com.moodsound.backend.controller;

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
    public ResponseEntity<?> getPlaylist(@PathVariable String moodName) {
        Mood mood = moodService.getMoodByName(moodName);

        if (mood == null) {
            ErrorResponse error = new ErrorResponse("Mood no encontrado: " + moodName);
            return ResponseEntity.badRequest().body(error);
        }

        List<Track> tracks = trackService.getTracksByMoodName(moodName);

        if (tracks.isEmpty()) {
            tracks = cargarCancionesDesdeYouTube(mood, moodName);
        }

        PlaylistResponse response = new PlaylistResponse();
        response.setMood(mood.getName());
        response.setDisplayName(mood.getDisplayName());
        response.setEmoji(mood.getEmoji());
        response.setColor(mood.getColor());
        response.setTracks(tracks);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{moodName}/refresh")
    public ResponseEntity<?> refreshPlaylist(@PathVariable String moodName) {
        Mood mood = moodService.getMoodByName(moodName);

        if (mood == null) {
            ErrorResponse error = new ErrorResponse("Mood no encontrado: " + moodName);
            return ResponseEntity.badRequest().body(error);
        }

        trackService.removeAllTracksFromMood(mood.getId());
        List<Track> tracks = cargarCancionesDesdeYouTube(mood, moodName);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Playlist actualizada con " + tracks.size() + " canciones");
        response.put("count", tracks.size());
        return ResponseEntity.ok(response);
    }

    private List<Track> cargarCancionesDesdeYouTube(Mood mood, String moodName) {
        List<YouTubeVideo> youtubeVideos = youtubeService.searchByMood(moodName, 15);

        int position = 1;
        for (YouTubeVideo video : youtubeVideos) {
            Track track = new Track();
            track.setYoutubeId(video.getVideoId());
            track.setTitle(video.getTitle());
            track.setArtist(video.getChannelTitle());
            track.setThumbnailUrl(video.getThumbnailUrl());
            track.setExternalUrl("https://music.youtube.com/watch?v=" + video.getVideoId());

            Track savedTrack = trackService.saveTrack(track);
            trackService.addTrackToMood(mood.getId(), savedTrack.getId(), position);
            position++;
        }

        return trackService.getTracksByMoodName(moodName);
    }
}