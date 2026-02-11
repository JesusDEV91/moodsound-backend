package com.moodsound.backend.repository;

import com.moodsound.backend.model.Favorite;
import com.moodsound.backend.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    @Query("SELECT f.track FROM Favorite f WHERE f.user.id = :userId ORDER BY f.addedAt DESC")
    List<Track> findTracksByUserId(@Param("userId") Integer userId);

    Optional<Favorite> findByUserIdAndTrackId(Integer userId, Integer trackId);

    Boolean existsByUserIdAndTrackId(Integer userId, Integer trackId);

    void deleteByUserIdAndTrackId(Integer userId, Integer trackId);

    Long countByUserId(Integer userId);
}