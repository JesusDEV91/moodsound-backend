package com.moodsound.backend.controller;

import com.moodsound.backend.model.Favorite;
import com.moodsound.backend.model.Track;
import com.moodsound.backend.response.ErrorResponse;
import com.moodsound.backend.service.FavoriteService;
import com.moodsound.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getFavorites(@RequestHeader("Authorization") String authHeader) {
        try {
            Integer userId = getUserIdFromAuth(authHeader);
            if (userId == null) {
                return ResponseEntity.status(401).body(new ErrorResponse("No autorizado"));
            }

            List<Track> favorites = favoriteService.getFavoritesByUserId(userId);
            return ResponseEntity.ok(favorites);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error al obtener favoritos"));
        }
    }

    @PostMapping("/add/{trackId}")
    public ResponseEntity<?> addFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer trackId) {

        try {
            Integer userId = getUserIdFromAuth(authHeader);
            if (userId == null) {
                return ResponseEntity.status(401).body(new ErrorResponse("No autorizado"));
            }

            Favorite favorite = favoriteService.addFavorite(userId, trackId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Canción añadida a favoritos");
            response.put("favoriteId", favorite.getId());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error al añadir favorito"));
        }
    }

    @DeleteMapping("/remove/{trackId}")
    public ResponseEntity<?> removeFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer trackId) {

        try {
            Integer userId = getUserIdFromAuth(authHeader);
            if (userId == null) {
                return ResponseEntity.status(401).body(new ErrorResponse("No autorizado"));
            }

            favoriteService.removeFavorite(userId, trackId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Canción eliminada de favoritos");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error al eliminar favorito"));
        }
    }

    @GetMapping("/check/{trackId}")
    public ResponseEntity<?> checkFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer trackId) {

        try {
            Integer userId = getUserIdFromAuth(authHeader);
            if (userId == null) {
                return ResponseEntity.status(401).body(new ErrorResponse("No autorizado"));
            }

            boolean isFavorite = favoriteService.isFavorite(userId, trackId);

            Map<String, Boolean> response = new HashMap<>();
            response.put("isFavorite", isFavorite);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error al verificar favorito"));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countFavorites(@RequestHeader("Authorization") String authHeader) {
        try {
            Integer userId = getUserIdFromAuth(authHeader);
            if (userId == null) {
                return ResponseEntity.status(401).body(new ErrorResponse("No autorizado"));
            }

            Long count = favoriteService.countFavorites(userId);

            Map<String, Long> response = new HashMap<>();
            response.put("count", count);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error al contar favoritos"));
        }
    }

    private Integer getUserIdFromAuth(String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            return userService.getUserIdFromToken(token);
        } catch (Exception e) {
            return null;
        }
    }
}