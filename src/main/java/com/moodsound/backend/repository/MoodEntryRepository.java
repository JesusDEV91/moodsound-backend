package com.moodsound.backend.repository;

import com.moodsound.backend.model.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, Integer> {
    List<MoodEntry> findByUserIdOrderByCreatedAtDesc(Integer userId);
}