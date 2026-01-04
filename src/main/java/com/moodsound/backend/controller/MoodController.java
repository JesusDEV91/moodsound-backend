package com.moodsound.backend.controller;

import com.moodsound.backend.model.Mood;
import com.moodsound.backend.response.AnalyzeRequest;
import com.moodsound.backend.response.ErrorResponse;
import com.moodsound.backend.response.MoodAnalysisResponse;
import com.moodsound.backend.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mood")
@CrossOrigin(origins = "*")
public class MoodController {

    @Autowired
    private MoodService moodService;

    /**
     * POST /api/mood/analyze
     * Analiza un texto o recibe un mood seleccionado
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeMood(@RequestBody AnalyzeRequest request) {

        String text = request.getText();
        String moodOption = request.getMoodOption();

        // Si el usuario seleccionó un botón directamente
        if (moodOption != null && !moodOption.isEmpty()) {
            Mood mood = moodService.getMoodByName(moodOption);

            if (mood == null) {
                ErrorResponse error = new ErrorResponse("Mood no válido");
                return ResponseEntity.badRequest().body(error);
            }

            // Crear respuesta
            MoodAnalysisResponse response = new MoodAnalysisResponse();
            response.setDetected(true);
            response.setMood(mood.getName());
            response.setMoodId(mood.getId());
            response.setDisplayName(mood.getDisplayName());
            response.setEmoji(mood.getEmoji());

            return ResponseEntity.ok(response);
        }

        // Si escribió texto libre, analizar
        if (text != null && !text.isEmpty()) {
            Map<String, Object> result = moodService.analyzeMood(text);

            MoodAnalysisResponse response = new MoodAnalysisResponse();
            response.setDetected((Boolean) result.get("detected"));

            if (result.get("mood") != null) {
                response.setMood((String) result.get("mood"));
                response.setMoodId((Integer) result.get("moodId"));
                response.setDisplayName((String) result.get("displayName"));
                response.setEmoji((String) result.get("emoji"));
            }

            if (result.get("message") != null) {
                response.setMessage((String) result.get("message"));
            }

            return ResponseEntity.ok(response);
        }

        // No envió ni texto ni opción
        MoodAnalysisResponse response = new MoodAnalysisResponse();
        response.setDetected(false);
        response.setMessage("Debes enviar 'text' o 'moodOption'");

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * GET /api/mood/all
     * Obtiene todos los moods disponibles
     */
    @GetMapping("/all")
    public ResponseEntity<List<Mood>> getAllMoods() {
        List<Mood> moods = moodService.getAllMoods();
        return ResponseEntity.ok(moods);
    }
}