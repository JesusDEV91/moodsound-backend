package com.moodsound.backend.service;

import com.moodsound.backend.model.User;
import com.moodsound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User getUserByUid(String uid) {
        return userRepository.findByUid(uid).orElse(null);
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


    public User createOrUpdateUser(String uid, String email, String username, String avatarUrl) {
        // Buscar si ya existe
        User user = userRepository.findByUid(uid).orElse(null);

        if (user != null) {
            // Usuario ya existe, actualizar sus datos
            user.setUsername(username);
            user.setEmail(email);
            user.setAvatarUrl(avatarUrl);
            return userRepository.save(user);
        } else {
            // Crear nuevo usuario
            User newUser = new User();
            newUser.setUid(uid);
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setAvatarUrl(avatarUrl);
            return userRepository.save(newUser);
        }
    }
}