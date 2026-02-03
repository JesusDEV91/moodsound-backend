package com.moodsound.backend;

import com.moodsound.backend.model.Mood;
import com.moodsound.backend.repository.MoodRepository;
import com.moodsound.backend.service.MoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoodServiceTest {

    @Mock
    private MoodRepository moodRepository;

    @InjectMocks
    private MoodService moodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAnalyzeMood_Happy() {

        Mood happyMood = new Mood();
        happyMood.setId(1);
        happyMood.setName("happy");
        happyMood.setDisplayName("Feliz");
        when(moodRepository.findByName("happy")).thenReturn(Optional.of(happyMood));
        Map<String, Object> result = moodService.analyzeMood("Me siento muy feliz hoy");
        assertTrue((Boolean) result.get("detected"));
        assertEquals("happy", result.get("mood"));
        assertEquals("Feliz", result.get("displayName"));
    }

    @Test
    void testAnalyzeMood_NoDetection_Fallback() {

        Map<String, Object> result = moodService.analyzeMood("pepe");
        assertFalse((Boolean) result.get("detected"));
        assertEquals("No hemos podido detectar tu estado de ánimo. Selecciona uno:", result.get("message"));
    }
}