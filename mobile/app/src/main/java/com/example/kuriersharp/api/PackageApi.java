package com.example.kuriersharp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface PackageApiService {
    // Zmień "your_endpoint/upload-photo" na rzeczywisty endpoint
    @POST("api/package/upload-photo")
    Call<PhotoUploadResponse> uploadPhoto(@Body PhotoUploadRequest request);
}

public class PackageApi {
    // ZMIEŃ NA ADRES TWOJEGO SERWERA DOCKER!
    // Jeśli testujesz na emulatorze, a serwer Docker jest na tym samym komputerze (localhost),
    // użyj 10.0.2.2 zamiast localhost lub 127.0.0.1
    private static final String BASE_URL = "http://10.0.2.2:5000/"; // PRZYKŁAD! Ustaw swój port.
    private PackageApiService service;

    public PackageApi() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Loguje ciało żądania i odpowiedzi

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging); // Dodaj interceptor tylko w trybie DEBUG

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        service = retrofit.create(PackageApiService.class);
    }

    public void uploadPhoto(PhotoUploadRequest request, Callback<PhotoUploadResponse> callback) {
        Call<PhotoUploadResponse> call = service.uploadPhoto(request);
        call.enqueue(callback);
    }
}