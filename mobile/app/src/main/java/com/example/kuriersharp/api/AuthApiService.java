package com.example.kuriersharp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.kuriersharp.api.LoginRequest; // Importuj swoje DTO
import com.example.kuriersharp.api.LoginResponse; // Importuj swoje DTO

public interface AuthApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}