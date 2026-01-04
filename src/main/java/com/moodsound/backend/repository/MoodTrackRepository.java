package com.moodsound.backend.repository;

import com.moodsound.backend.model.MoodTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MoodTrackRepository extends JpaRepository<MoodTrack, Integer> {

    @Query("SELECT mt FROM MoodTrack mt JOIN FETCH mt.track WHERE mt.mood.id = :moodId ORDER BY mt.orderPosition")
    List<MoodTrack> findByMoodIdOrderByOrderPosition(@Param("moodId") Integer moodId);

    boolean existsByMoodIdAndTrackId(Integer moodId, Integer trackId);

    void deleteByMoodId(Integer moodId);
}