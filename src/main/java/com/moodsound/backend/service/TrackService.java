package com.moodsound.backend.service;

import com.moodsound.backend.model.AudienceType;
import com.moodsound.backend.model.Mood;
import com.moodsound.backend.model.MoodTrack;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.repository.MoodRepository;
import com.moodsound.backend.repository.MoodTrackRepository;
import com.moodsound.backend.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private MoodTrackRepository moodTrackRepository;

    @Autowired
    private MoodRepository moodRepository;

    public List<Track> getTracksByMoodAndAudience(String moodName, AudienceType audience) {

        return trackRepository.findByMoodAndAudience(moodName, audience);
    }

    public List<Track> getTracksByMood(Integer moodId) {
        List<MoodTrack> moodTracks = moodTrackRepository.findByMoodIdOrderByOrderPosition(moodId);


        return moodTracks.stream()
                .map(MoodTrack::getTrack)
                .collect(Collectors.toList());
    }

    public List<Track> getTracksByMoodName(String moodName) {
        return moodRepository.findByName(moodName)
                .map(mood -> getTracksByMood(mood.getId()))
                .orElse(new ArrayList<>());
    }

    public Track saveTrack(Track track) {

        return trackRepository.findByYoutubeId(track.getYoutubeId())
                .orElseGet(() -> trackRepository.save(track));
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

    @Transactional
    public void removeAllTracksFromMood(Integer moodId) {
        moodTrackRepository.deleteByMoodId(moodId);
    }

    public int countTracksByMood(Integer moodId) {
        return moodTrackRepository.findByMoodIdOrderByOrderPosition(moodId).size();
    }
}