package com.moodsound.backend.repository;

import com.moodsound.backend.model.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MoodRepository extends JpaRepository<Mood, Integer> {
    Optional<Mood> findByName(String name);
}