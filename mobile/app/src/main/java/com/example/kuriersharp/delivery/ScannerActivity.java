package com.example.kuriersharp.delivery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kuriersharp.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScannerActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 10;
    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private BarcodeAnalyzer barcodeAnalyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        previewView = findViewById(R.id.previewView);
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Sprawdzamy uprawnienia do kamery
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // Obsługa błędów
                Toast.makeText(this, "Błąd uruchamiania kamery: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Analizator kodów kreskowych/QR
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        barcodeAnalyzer = new BarcodeAnalyzer(barcode -> {
            // Tutaj otrzymujemy zeskanowany kod
            String scannedValue = barcode.getRawValue();
            if (scannedValue != null) {
                runOnUiThread(() -> {
                    // Zatrzymujemy kamerę po zeskanowaniu
                    cameraProvider.unbindAll();

                    // Przekazujemy zeskanowany kod do aktywności szczegółów przesyłki
                    Intent intent = new Intent(ScannerActivity.this, DeliveryDetailsActivity.class);
                    intent.putExtra("TRACKING_NUMBER", scannedValue);
                    startActivity(intent);
                    finish();
                });
            }
        });

        imageAnalysis.setAnalyzer(cameraExecutor, barcodeAnalyzer);
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Wymagane są uprawnienia do kamery",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    // Klasa pomocnicza dla ML Kit do analizy kodów kreskowych/QR
    // W rzeczywistej implementacji musisz dodać zależność ML Kit i zaimplementować tę klasę
    private static class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
        // Placeholder klasy, która wymaga implementacji z ML Kit
        public interface BarcodeListener {
            void onBarcodeDetected(Barcode barcode);
        }

        private final BarcodeListener listener;

        public BarcodeAnalyzer(BarcodeListener listener) {
            this.listener = listener;
        }

        @Override
        public void analyze(@NonNull ImageProxy image) {
            // Tutaj byłaby implementacja wykrywania kodów z ML Kit
            // Na potrzeby demo tworzymy placeholder
            image.close();
        }
    }

    // Placeholder dla klasy Barcode z ML Kit
    private static class Barcode {
        public String getRawValue() {
            return "SAMPLE_TRACKING_NUMBER";
        }
    }

    private static class ImageProxy {
        public void close() {
            // Placeholder metody
        }
    }
}