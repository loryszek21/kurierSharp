package com.example.kuriersharp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64; // Potrzebny import
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar; // Dla wskaźnika postępu
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.kuriersharp.api.PackageApi; // Załóżmy, że PackageApi jest zaimplementowane
import com.example.kuriersharp.api.PhotoUploadRequest; // Model żądania dla Retrofit
import com.example.kuriersharp.api.PhotoUploadResponse; // Model odpowiedzi dla Retrofit
import com.example.kuriersharp.model.Package; // Twój model paczki

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call; // Dla Retrofit
import retrofit2.Callback; // Dla Retrofit
import retrofit2.Response; // Dla Retrofit

public class AddPhotoActivity extends AppCompatActivity {

    private static final String TAG = "AddPhotoActivity";
    // Załóżmy, że ID = 0 oznacza "brak ID" dla typu int.
    // Jeśli używasz innej wartości (np. -1) lub jeśli 0 jest poprawnym ID,
    // dostosuj poniższą stałą i jej użycie.
    private static final int INVALID_PACKAGE_ID_SENTINEL = 0;


    private String currentPhotoPath; // Przechowuje ścieżkę do OSTATECZNEGO pliku zdjęcia (z aparatu lub galerii)
    private Uri currentPhotoUri;    // Przechowuje URI do OSTATECZNEGO pliku zdjęcia
    private ImageView imageView;
    private Package currentPackage; // Załóżmy, że model Package ma metodę getId() zwracającą int
    private Button saveButton;
    private ProgressBar progressBar;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    private PackageApi packageApi; // Instancja Twojego API klienta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo); // Upewnij się, że masz ten layout

        imageView = findViewById(R.id.imageView);
        Button takePhotoButton = findViewById(R.id.takePhotoButton);
        Button chooseFromGalleryButton = findViewById(R.id.chooseFromGalleryButton);
        saveButton = findViewById(R.id.saveButton);
        progressBar = findViewById(R.id.progressBar); // Dodaj ProgressBar do layoutu activity_add_photo.xml

        packageApi = new PackageApi();

        currentPackage = getIntent().getParcelableExtra("package");
        // POPRAWKA: Porównanie currentPackage.getId() z wartością wartowniczą zamiast null
        if (currentPackage == null || currentPackage.getId() == INVALID_PACKAGE_ID_SENTINEL) {
            Toast.makeText(this, "Błąd: Brak informacji o paczce lub nieprawidłowe ID.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "currentPackage is null or has invalid ID: " + (currentPackage != null ? currentPackage.getId() : "package is null"));
            finish();
            return;
        }

        setupLaunchers();

        takePhotoButton.setOnClickListener(v -> checkPermissionsAndOpenCamera());
        chooseFromGalleryButton.setOnClickListener(v -> openGallery());
        saveButton.setOnClickListener(v -> saveAndUploadPhoto());
    }

    private void setupLaunchers() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (currentPhotoUri != null) {
                            Log.d(TAG, "Photo taken, URI: " + currentPhotoUri.toString());
                            displayImage(currentPhotoUri);
                        } else {
                            Log.e(TAG, "Photo taken but currentPhotoUri is null");
                        }
                    } else {
                        Log.d(TAG, "Camera action cancelled or failed.");
                        currentPhotoPath = null;
                        currentPhotoUri = null;
                    }
                });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Boolean cameraGranted = result.getOrDefault(Manifest.permission.CAMERA, false);
            if (cameraGranted) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Pozwolenie na użycie aparatu nie zostało udzielone", Toast.LENGTH_SHORT).show();
            }
        });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            Log.d(TAG, "Photo selected from gallery, URI: " + selectedImageUri.toString());
                            try {
                                File localFile = createImageFileFromUri(selectedImageUri);
                                if (localFile != null && localFile.exists()) {
                                    currentPhotoPath = localFile.getAbsolutePath();
                                    currentPhotoUri = FileProvider.getUriForFile(this,
                                            getApplicationContext().getPackageName() + ".fileprovider",
                                            localFile);
                                    Log.d(TAG, "Gallery photo saved to: " + currentPhotoPath + ", URI: " + currentPhotoUri);
                                    displayImage(currentPhotoUri);
                                } else {
                                    Toast.makeText(this, "Nie udało się zapisać zdjęcia z galerii.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                Log.e(TAG, "Error processing gallery image", e);
                                Toast.makeText(this, "Błąd przetwarzania zdjęcia z galerii", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.d(TAG, "Gallery selection cancelled or failed.");
                    }
                });
    }

    private void checkPermissionsAndOpenCamera() {
        String[] permissionsToRequest;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissionsToRequest = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else {
            permissionsToRequest = new String[]{Manifest.permission.CAMERA};
        }

        boolean allPermissionsGranted = true;
        for (String perm : permissionsToRequest) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (allPermissionsGranted) {
            dispatchTakePictureIntent();
        } else {
            permissionLauncher.launch(permissionsToRequest);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFileForCamera();
            } catch (IOException ex) {
                Log.e(TAG, "Error creating image file for camera", ex);
                Toast.makeText(this, "Błąd tworzenia pliku zdjęcia", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                currentPhotoUri = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
                Log.d(TAG, "Launching camera, output URI: " + currentPhotoUri);
                cameraLauncher.launch(takePictureIntent);
            }
        } else {
            Toast.makeText(this, "Nie znaleziono aplikacji aparatu", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFileForCamera() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        // POPRAWKA: Porównanie currentPackage.getId() z wartością wartowniczą
        String packageIdPart = (currentPackage != null && currentPackage.getId() != INVALID_PACKAGE_ID_SENTINEL)
                ? currentPackage.getId() + "_"
                : "UNKNOWN_ID_";
        String imageFileName = "CAMERA_" + packageIdPart + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null || (!storageDir.exists() && !storageDir.mkdirs())) {
            Log.e(TAG, "Failed to create directory: " + (storageDir != null ? storageDir.getAbsolutePath() : "null"));
            throw new IOException("Nie można utworzyć katalogu na zdjęcia.");
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "Camera image file created: " + currentPhotoPath);
        return image;
    }

    private File createImageFileFromUri(Uri sourceUri) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        // POPRAWKA: Porównanie currentPackage.getId() z wartością wartowniczą
        String packageIdPart = (currentPackage != null && currentPackage.getId() != INVALID_PACKAGE_ID_SENTINEL)
                ? currentPackage.getId() + "_"
                : "UNKNOWN_ID_";
        String imageFileName = "GALLERY_" + packageIdPart + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null || (!storageDir.exists() && !storageDir.mkdirs())) {
            Log.e(TAG, "Failed to create directory for gallery image: " + (storageDir != null ? storageDir.getAbsolutePath() : "null"));
            throw new IOException("Nie można utworzyć katalogu na zdjęcia.");
        }
        File destinationFile = new File(storageDir, imageFileName);

        ContentResolver resolver = getContentResolver();
        try (InputStream inputStream = resolver.openInputStream(sourceUri);
             OutputStream outputStream = new FileOutputStream(destinationFile)) {
            if (inputStream == null) {
                throw new IOException("Nie można otworzyć strumienia wejściowego dla URI: " + sourceUri);
            }
            byte[] buf = new byte[8192];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
        }
        Log.d(TAG, "Gallery image copied to: " + destinationFile.getAbsolutePath());
        return destinationFile;
    }

    private void displayImage(Uri imageUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream stream = getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(stream, null, options);
            if (stream != null) stream.close();

            int reqWidth = imageView.getWidth() > 0 ? imageView.getWidth() : 512;
            int reqHeight = imageView.getHeight() > 0 ? imageView.getHeight() : 512;

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            stream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
            if (stream != null) stream.close();

            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.e(TAG, "Error displaying image", e);
            Toast.makeText(this, "Błąd wyświetlania zdjęcia", Toast.LENGTH_SHORT).show();
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    private String convertImageFileToBase64(String filePath) {
        if (filePath == null || filePath.isEmpty()) return null;
        File imageFile = new File(filePath);
        if (!imageFile.exists() || !imageFile.canRead()) {
            Log.e(TAG, "Cannot read image file: " + filePath);
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inSampleSize = 2; // Możesz odkomentować, aby zmniejszyć rozmiar przed wysłaniem

        try (InputStream inputStream = new FileInputStream(imageFile)) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            if (bitmap == null) {
                Log.e(TAG, "BitmapFactory.decodeStream returned null for " + filePath);
                return null;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            bitmap.recycle();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (IOException e) {
            Log.e(TAG, "Error converting image to Base64", e);
            return null;
        }
    }

    private void saveAndUploadPhoto() {
        if (currentPhotoPath == null || currentPhotoPath.isEmpty()) {
            Toast.makeText(this, "Najpierw zrób lub wybierz zdjęcie.", Toast.LENGTH_SHORT).show();
            return;
        }

        String base64Image = convertImageFileToBase64(currentPhotoPath);

        if (base64Image == null) {
            Toast.makeText(this, "Nie udało się przekonwertować zdjęcia.", Toast.LENGTH_SHORT).show();
            return;
        }
        // POPRAWKA: Porównanie currentPackage.getId() z wartością wartowniczą
        if (currentPackage == null || currentPackage.getId() == INVALID_PACKAGE_ID_SENTINEL) {
            Toast.makeText(this, "Błąd: Brak ID paczki do powiązania ze zdjęciem.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Cannot upload, currentPackage or its ID is invalid.");
            return;
        }

        // Używamy getId() bezpośrednio, bo jest to int
        String packageIdString = String.valueOf(currentPackage.getId());
        Log.d(TAG, "Uploading photo for package ID: " + packageIdString + ", Base64 length: " + base64Image.length());

        setLoadingState(true);

        PhotoUploadRequest request = new PhotoUploadRequest(packageIdString, base64Image);
        packageApi.uploadPhoto(request, new Callback<PhotoUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<PhotoUploadResponse> call, @NonNull Response<PhotoUploadResponse> response) {
                setLoadingState(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Upload successful: " + response.body().getMessage());
                    Toast.makeText(AddPhotoActivity.this, "Zdjęcie wysłane: " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String errorMsg = "Błąd wysyłania zdjęcia";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ": " + response.errorBody().string() + " (Kod: " + response.code() + ")";
                        } catch (IOException e) {
                            Log.e(TAG, "Error parsing error body", e);
                        }
                    } else {
                        errorMsg += " (Kod: " + response.code() + ")";
                    }
                    Log.e(TAG, "Upload error: " + errorMsg);
                    Toast.makeText(AddPhotoActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoUploadResponse> call, @NonNull Throwable t) {
                setLoadingState(false);
                Log.e(TAG, "Upload failure (network or other)", t);
                Toast.makeText(AddPhotoActivity.this, "Błąd sieci: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            saveButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            saveButton.setEnabled(true);
        }
    }
}