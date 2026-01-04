package com.moodsound.backend.response;

public class FavoriteCheckResponse {
    private boolean isFavorite;

    // Constructor vacío
    public FavoriteCheckResponse() {
    }

    // Constructor con valor
    public FavoriteCheckResponse(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    // Getter y Setter
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}