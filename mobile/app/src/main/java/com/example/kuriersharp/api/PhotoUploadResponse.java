package com.example.kuriersharp.api;

public class PhotoUploadResponse {
    String status;
    String message;
    String imageUrl; // Opcjonalnie, jeśli serwer zwraca URL do zapisanego zdjęcia

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getImageUrl() { return imageUrl; }
}
