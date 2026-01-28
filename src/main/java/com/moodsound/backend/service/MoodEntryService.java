package com.moodsound.backend.service;

import com.moodsound.backend.model.Mood;
import com.moodsound.backend.model.MoodEntry;
import com.moodsound.backend.model.User;
import com.moodsound.backend.repository.MoodEntryRepository;
import com.moodsound.backend.repository.MoodRepository;
import com.moodsound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MoodEntryService {

    @Autowired
    private MoodEntryRepository moodEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MoodRepository moodRepository;

    public List<MoodEntry> getHistoryByUserId(Integer userId) {
        return moodEntryRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void saveMoodEntry(Integer userId, Integer moodId, String inputText, String detectedBy) {
        User user = userRepository.findById(userId).orElse(null);
        Mood mood = moodRepository.findById(moodId).orElse(null);

        if (user != null && mood != null) {
            MoodEntry entry = new MoodEntry();
            entry.setUser(user);
            entry.setMood(mood);
            entry.setInputText(inputText);
            entry.setDetectedBy(detectedBy);
            moodEntryRepository.save(entry);
        }
    }
}