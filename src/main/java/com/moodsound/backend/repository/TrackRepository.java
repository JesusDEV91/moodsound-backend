package com.moodsound.backend.repository;

import com.moodsound.backend.model.AudienceType;
import com.moodsound.backend.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {
    Optional<Track> findByYoutubeId(String youtubeId);
    boolean existsByYoutubeId(String youtubeId);

    @Query("SELECT t FROM Track t " +
            "JOIN t.moodTracks mt " +
            "JOIN mt.mood m " +
            "WHERE m.name = :moodName " +
            "AND t.audienceType = :audience " +
            "ORDER BY mt.orderPosition ASC")
    List<Track> findByMoodAndAudience(@Param("moodName") String moodName,
                                      @Param("audience") AudienceType audience);
}