package com.moodsound.backend.service;

import com.moodsound.backend.model.Mood;
import com.moodsound.backend.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MoodService {

    @Autowired
    private MoodRepository moodRepository;


    public Map<String, Object> analyzeMood(String inputText) {
        Map<String, Object> result = new HashMap<>();

        if (inputText == null || inputText.trim().isEmpty()) {
            result.put("detected", false);
            result.put("message", "No has escrito nada. Selecciona un estado de ánimo:");
            return result;
        }


        String texto = inputText.toLowerCase();


        if (texto.contains("feliz") || texto.contains("alegre") || texto.contains("contento")
                || texto.contains("bien") || texto.contains("genial")) {

            Mood mood = moodRepository.findByName("happy").orElse(null);
            if (mood != null) {
                result.put("detected", true);
                result.put("mood", "happy");
                result.put("moodId", mood.getId());
                result.put("displayName", mood.getDisplayName());
                result.put("emoji", mood.getEmoji());
                return result;
            }
        }


        if (texto.contains("triste") || texto.contains("mal") || texto.contains("solo")
                || texto.contains("llorar") || texto.contains("deprimido")) {

            Mood mood = moodRepository.findByName("sad").orElse(null);
            if (mood != null) {
                result.put("detected", true);
                result.put("mood", "sad");
                result.put("moodId", mood.getId());
                result.put("displayName", mood.getDisplayName());
                result.put("emoji", mood.getEmoji());
                return result;
            }
        }


        if (texto.contains("energía") || texto.contains("enérgico") || texto.contains("motivado")
                || texto.contains("gym") || texto.contains("ejercicio") || texto.contains("deporte")) {

            Mood mood = moodRepository.findByName("energetic").orElse(null);
            if (mood != null) {
                result.put("detected", true);
                result.put("mood", "energetic");
                result.put("moodId", mood.getId());
                result.put("displayName", mood.getDisplayName());
                result.put("emoji", mood.getEmoji());
                return result;
            }
        }


        if (texto.contains("relajado") || texto.contains("tranquilo") || texto.contains("cansado")
                || texto.contains("dormir") || texto.contains("descansar")) {

            Mood mood = moodRepository.findByName("chill").orElse(null);
            if (mood != null) {
                result.put("detected", true);
                result.put("mood", "chill");
                result.put("moodId", mood.getId());
                result.put("displayName", mood.getDisplayName());
                result.put("emoji", mood.getEmoji());
                return result;
            }
        }


        result.put("detected", false);
        result.put("message", "No hemos podido detectar tu estado de ánimo. Selecciona uno:");
        return result;
    }

    public List<Mood> getAllMoods() {
        return moodRepository.findAll();
    }


    public Mood getMoodByName(String name) {
        return moodRepository.findByName(name).orElse(null);
    }
}