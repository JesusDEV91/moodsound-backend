package com.moodsound.backend.service;

import com.moodsound.backend.model.Mood;
import com.moodsound.backend.model.MoodTrack;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.repository.MoodRepository;
import com.moodsound.backend.repository.MoodTrackRepository;
import com.moodsound.backend.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;  // ✅ AGREGAR ESTE IMPORT

import java.util.ArrayList;
import java.util.List;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private MoodTrackRepository moodTrackRepository;

    @Autowired
    private MoodRepository moodRepository;

    public List<Track> getTracksByMood(Integer moodId) {
        List<MoodTrack> moodTracks = moodTrackRepository.findByMoodIdOrderByOrderPosition(moodId);

        List<Track> tracks = new ArrayList<>();
        for (MoodTrack moodTrack : moodTracks) {
            tracks.add(moodTrack.getTrack());
        }

        return tracks;
    }

    public List<Track> getTracksByMoodName(String moodName) {
        Mood mood = moodRepository.findByName(moodName).orElse(null);

        if (mood == null) {
            return new ArrayList<>();
        }

        return getTracksByMood(mood.getId());
    }

    public Track saveTrack(Track track) {
        Track existing = trackRepository.findByYoutubeId(track.getYoutubeId()).orElse(null);

        if (existing != null) {
            return existing;
        }

        return trackRepository.save(track);
    }

    public void addTrackToMood(Integer moodId, Integer trackId, Integer position) {
        if (moodTrackRepository.existsByMoodIdAndTrackId(moodId, trackId)) {
            return;
        }

        Mood mood = moodRepository.findById(moodId).orElse(null);
        Track track = trackRepository.findById(trackId).orElse(null);

        if (mood != null && track != null) {
            MoodTrack moodTrack = new MoodTrack();
            moodTrack.setMood(mood);
            moodTrack.setTrack(track);
            moodTrack.setOrderPosition(position);
            moodTrackRepository.save(moodTrack);
        }
    }

    @Transactional  // ✅ AGREGAR ESTA ANOTACIÓN
    public void removeAllTracksFromMood(Integer moodId) {
        moodTrackRepository.deleteByMoodId(moodId);
    }

    public int countTracksByMood(Integer moodId) {
        List<MoodTrack> moodTracks = moodTrackRepository.findByMoodIdOrderByOrderPosition(moodId);
        return moodTracks.size();
    }
}