package com.moodsound.backend.service;

import com.moodsound.backend.model.User;
import com.moodsound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String username, String email, String password, String fullName) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }


        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setFullName(fullName);
        user.setActive(true);

        return userRepository.save(user);
    }

    public Optional<User> loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (BCrypt.checkpw(password, user.getPassword()) && user.getActive()) {
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    private String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public String generateToken(User user) {
        String tokenData = user.getId() + ":" + user.getUsername() + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
    }

    public Integer getUserIdFromToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            return Integer.parseInt(parts[0]);
        } catch (Exception e) {
            return null;
        }
    }
}