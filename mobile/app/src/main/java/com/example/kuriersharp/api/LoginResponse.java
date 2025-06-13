package com.example.kuriersharp.api;

public class LoginResponse {
    boolean success;
    String message;
    String userEmail;
    String userName;
    int userId;

    // Gettery
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getUserEmail() { return userEmail; }
    public String getUserName() { return userName; }
    public int getUserId() { return userId; }
}