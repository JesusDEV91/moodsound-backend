package com.moodsound.backend.repository;

import com.moodsound.backend.model.FavoriteTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FavoriteTrackRepository extends JpaRepository<FavoriteTrack, Integer> {

    @Query("SELECT ft FROM FavoriteTrack ft JOIN FETCH ft.track WHERE ft.user.id = :userId ORDER BY ft.addedAt DESC")
    List<FavoriteTrack> findByUserIdOrderByAddedAtDesc(@Param("userId") Integer userId);

    boolean existsByUserIdAndTrackId(Integer userId, Integer trackId);

    void deleteByUserIdAndTrackId(Integer userId, Integer trackId);
}