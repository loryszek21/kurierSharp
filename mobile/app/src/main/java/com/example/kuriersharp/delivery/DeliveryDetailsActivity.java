package com.example.kuriersharp.delivery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuriersharp.R;
import com.example.kuriersharp.delivery.model.Delivery;
import com.example.kuriersharp.delivery.model.DeliveryStatus;
import com.example.kuriersharp.utils.EmailSender;
import com.example.kuriersharp.utils.PDFGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeliveryDetailsActivity extends AppCompatActivity {

    private TextView trackingNumberText;
    private TextView recipientNameText;
    private TextView recipientAddressText;
    private TextView recipientPhoneText;
    private TextView statusText;
    private TextView deliveryDateText;
    private TextView notesText;
    private RecyclerView photosRecyclerView;
    private PhotoAdapter photoAdapter;
    private Button statusUpdateButton;
    private Button pdfButton;
    private Button emailButton;
    private Button mapButton;
    private FloatingActionButton cameraFab;

    private Delivery delivery;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        // Inicjalizacja widoków
        initViews();

        // Pobierz ID przesyłki z intentu
        String deliveryId = getIntent().getStringExtra("DELIVERY_ID");
        String trackingNumber = getIntent().getStringExtra("TRACKING_NUMBER");

        if (deliveryId != null) {
            loadDeliveryDetails(deliveryId);
        } else if (trackingNumber != null) {
            loadDeliveryByTrackingNumber(trackingNumber);
        } else {
            Toast.makeText(this, "Błąd: Brak identyfikatora przesyłki", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupButtons();
    }

    private void initViews() {
        trackingNumberText = findViewById(R.id.trackingNumberText);
        recipientNameText = findViewById(R.id.recipientNameText);
        recipientAddressText = findViewById(R.id.recipientAddressText);
        recipientPhoneText = findViewById(R.id.recipientPhoneText);
        statusText = findViewById(R.id.statusText);
        deliveryDateText = findViewById(R.id.deliveryDateText);
        notesText = findViewById(R.id.notesText);
        photosRecyclerView = findViewById(R.id.photosRecyclerView);
        statusUpdateButton = findViewById(R.id.statusUpdateButton);
        pdfButton = findViewById(R.id.pdfButton);
        emailButton = findViewById(R.id.emailButton);
        mapButton = findViewById(R.id.mapButton);
        cameraFab = findViewById(R.id.cameraFab);

        // Inicjalizacja RecyclerView dla zdjęć
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<String> emptyList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(emptyList);
        photosRecyclerView.setAdapter(photoAdapter);
    }

    private void loadDeliveryDetails(String deliveryId) {
        // W rzeczywistej aplikacji, tutaj byłoby pobieranie danych z API lub bazy danych
        // Na potrzeby demo, tworzymy przykładową przesyłkę
        delivery = getMockDelivery(deliveryId);
        displayDeliveryDetails();
    }

    private void loadDeliveryByTrackingNumber(String trackingNumber) {
        // W rzeczywistej aplikacji, tutaj byłoby pobieranie danych na podstawie numeru śledzenia
        // Na potrzeby demo, tworzymy przykładową przesyłkę
        delivery = getMockDeliveryByTrackingNumber(trackingNumber);
        displayDeliveryDetails();
    }

    private void displayDeliveryDetails() {
        if (delivery == null) {
            Toast.makeText(this, "Nie znaleziono przesyłki", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        trackingNumberText.setText(delivery.getTrackingNumber());
        recipientNameText.setText(delivery.getRecipientName());
        recipientAddressText.setText(delivery.getRecipientAddress());
        recipientPhoneText.setText(delivery.getRecipientPhone());
        statusText.setText(delivery.getStatus().getDisplayName());

        if (delivery.getDeliveryDate() != null) {
            deliveryDateText.setText(dateFormat.format(delivery.getDeliveryDate()));
        } else {
            deliveryDateText.setText("--.--.----");
        }

        notesText.setText(delivery.getNotes());

        // Aktualizacja listy zdjęć
        if (delivery.getPhotosPaths() != null && !delivery.getPhotosPaths().isEmpty()) {
            photoAdapter.updatePhotos(delivery.getPhotosPaths());
        }

        // Aktualizacja przycisku statusu
        updateStatusButton();
    }

    private void updateStatusButton() {
        if (delivery.getStatus() == DeliveryStatus.NEW) {
            statusUpdateButton.setText("Rozpocznij dostawę");
        } else if (delivery.getStatus() == DeliveryStatus.IN_TRANSIT) {
            statusUpdateButton.setText("Oznacz jako dostarczone");
        } else {
            statusUpdateButton.setText("Aktualizuj status");
            statusUpdateButton.setEnabled(false);
        }
    }

    private void setupButtons() {
        // Przycisk aktualizacji statusu
        statusUpdateButton.setOnClickListener(v -> {
            if (delivery.getStatus() == DeliveryStatus.NEW) {
                delivery.setStatus(DeliveryStatus.IN_TRANSIT);
                Toast.makeText(this, "Status zmieniony na: W trakcie dostawy", Toast.LENGTH_SHORT).show();
            } else if (delivery.getStatus() == DeliveryStatus.IN_TRANSIT) {
                delivery.setStatus(DeliveryStatus.DELIVERED);
                delivery.setDeliveryDate(new Date());
                Toast.makeText(this, "Status zmieniony na: Doręczona", Toast.LENGTH_SHORT).show();
            }

            // Aktualizacja UI
            statusText.setText(delivery.getStatus().getDisplayName());
            if (delivery.getDeliveryDate() != null) {
                deliveryDateText.setText(dateFormat.format(delivery.getDeliveryDate()));
            }
            updateStatusButton();

            // Jeśli przesyłka została dostarczona, pokaż opcję wysłania maila
            if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
                emailButton.setVisibility(View.VISIBLE);
            }
        });

        // Przycisk eksportu do PDF
        pdfButton.setOnClickListener(v -> {
            PDFGenerator pdfGenerator = new PDFGenerator(this);
            String filePath = pdfGenerator.generateDeliveryReport(delivery);
            if (filePath != null) {
                Toast.makeText(this, "Raport zapisany: " + filePath, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Błąd podczas generowania PDF", Toast.LENGTH_SHORT).show();
            }
        });

        // Przycisk wysłania maila
        emailButton.setOnClickListener(v -> {
            if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
                EmailSender emailSender = new EmailSender(this);
                emailSender.sendDeliveryConfirmation(delivery);
            } else {
                Toast.makeText(this, "Przesyłka musi być dostarczona, aby wysłać powiadomienie",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Przycisk mapy
        mapButton.setOnClickListener(v -> {
            if (delivery.getLatitude() != 0 && delivery.getLongitude() != 0) {
                // Otwórz mapę z lokalizacją przesyłki
                Uri gmmIntentUri = Uri.parse("geo:" + delivery.getLatitude() + "," +
                        delivery.getLongitude() + "?q=" + Uri.encode(delivery.getRecipientAddress()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Nie znaleziono aplikacji map",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                // Otwórz mapę z adresem tekstowym
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(delivery.getRecipientAddress()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Nie znaleziono aplikacji map",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Przycisk aparatu
        cameraFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeliveryPhotoActivity.class);
            intent.putExtra("DELIVERY_ID", delivery.getId());
            startActivity(intent);
        });
    }

    private Delivery getMockDelivery(String deliveryId) {
        // Przykładowa przesyłka dla demonstracji
        Delivery mockDelivery = new Delivery(
                deliveryId,
                "PL" + deliveryId + "XYZ",
                "Jan Kowalski",
                "ul. Kwiatowa 1, Warszawa",
                "123456789",
                DeliveryStatus.IN_TRANSIT,
                new Date()
        );

        mockDelivery.setNotes("Przesyłka delikatna, proszę obchodzić się ostrożnie.");
        mockDelivery.setLatitude(52.2297);
        mockDelivery.setLongitude(21.0122);

        List<String> photos = new ArrayList<>();
        photos.add("https://example.com/photo1.jpg");
        mockDelivery.setPhotosPaths(photos);

        return mockDelivery;
    }

    private Delivery getMockDeliveryByTrackingNumber(String trackingNumber) {
        // Przykładowa przesyłka dla demonstracji
        Delivery mockDelivery = new Delivery(
                "123",
                trackingNumber,
                "Anna Nowak",
                "ul. Polna 5, Kraków",
                "987654321",
                DeliveryStatus.NEW,
                null
        );

        mockDelivery.setNotes("Dostawa do biura w godzinach 9-17.");
        mockDelivery.setLatitude(50.0647);
        mockDelivery.setLongitude(19.9450);

        return mockDelivery;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delivery_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            // Implementacja edycji przesyłki
            Toast.makeText(this, "Funkcja edycji przesyłki", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_delete) {
            // Implementacja usuwania przesyłki
            Toast.makeText(this, "Funkcja usuwania przesyłki", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}