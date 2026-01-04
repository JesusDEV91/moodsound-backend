package com.moodsound.backend.repository;

import com.moodsound.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUid(String uid);
    Optional<User> findByEmail(String email);
    boolean existsByUid(String uid);
}