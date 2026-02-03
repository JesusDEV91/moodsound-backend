package com.moodsound.backend;

import com.moodsound.backend.model.Track;
import com.moodsound.backend.repository.TrackRepository;
import com.moodsound.backend.service.TrackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    @Mock
    private TrackRepository trackRepository;

    @InjectMocks
    private TrackService trackService;

    @Test
    void testSaveTrack_ExistingTrack() {

        Track existingTrack = new Track();
        existingTrack.setYoutubeId("ABC123");

        when(trackRepository.findByYoutubeId("ABC123")).thenReturn(Optional.of(existingTrack));


        Track result = trackService.saveTrack(existingTrack);


        verify(trackRepository, times(0)).save(any());
        assertEquals("ABC123", result.getYoutubeId());
    }
}