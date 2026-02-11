package com.moodsound.backend.controller;

import com.moodsound.backend.dto.AuthResponse;
import com.moodsound.backend.dto.LoginRequest;
import com.moodsound.backend.dto.RegisterRequest;
import com.moodsound.backend.model.User;
import com.moodsound.backend.response.ErrorResponse;
import com.moodsound.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFullName()
            );

            String token = userService.generateToken(user);

            AuthResponse response = new AuthResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    token,
                    user.getCreatedAt() // ⬅️ AÑADIDO
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.loginUser(request.getUsername(), request.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = userService.generateToken(user);

            AuthResponse response = new AuthResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    token,
                    user.getCreatedAt() // ⬅️ AÑADIDO
            );

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body(new ErrorResponse("Credenciales inválidas"));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = userService.getUserIdFromToken(token);

            if (userId != null) {
                Optional<User> userOpt = userService.getUserById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    AuthResponse response = new AuthResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getFullName(),
                            token,
                            user.getCreatedAt() // ⬅️ AÑADIDO
                    );
                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.status(401).body(new ErrorResponse("Token inválido"));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ErrorResponse("Token inválido"));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = userService.getUserIdFromToken(token);

            if (userId != null) {
                Optional<User> userOpt = userService.getUserById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();

                    AuthResponse response = new AuthResponse();
                    response.setUserId(user.getId());
                    response.setUsername(user.getUsername());
                    response.setEmail(user.getEmail());
                    response.setFullName(user.getFullName());
                    response.setCreatedAt(user.getCreatedAt()); // ⬅️ AÑADIDO

                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.status(401).body(new ErrorResponse("Usuario no encontrado"));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ErrorResponse("Error al obtener perfil"));
        }
    }
}