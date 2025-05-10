package com.example.kuriersharp.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kuriersharp.R;
import com.example.kuriersharp.delivery.model.Delivery;
import com.example.kuriersharp.delivery.model.DeliveryStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeliveryAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyView;
    private FloatingActionButton scanFab;
    private List<Delivery> deliveryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);

        recyclerView = findViewById(R.id.deliveryRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        emptyView = findViewById(R.id.emptyView);
        scanFab = findViewById(R.id.scanFab);

        setupRecyclerView();
        loadDeliveries();

        swipeRefreshLayout.setOnRefreshListener(this::refreshDeliveries);

        scanFab.setOnClickListener(v -> {
            Intent intent = new Intent(DeliveryListActivity.this, ScannerActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        deliveryList = new ArrayList<>();
        adapter = new DeliveryAdapter(deliveryList, delivery -> {
            // Po kliknięciu w przesyłkę, przechodzimy do jej szczegółów
            Intent intent = new Intent(DeliveryListActivity.this, DeliveryDetailsActivity.class);
            intent.putExtra("DELIVERY_ID", delivery.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadDeliveries() {
        // Tutaj byłoby pobieranie przesyłek z API lub bazy danych
        // Na potrzeby demo, dodajemy przykładowe dane
        swipeRefreshLayout.setRefreshing(true);

        // W rzeczywistej aplikacji, dane byłyby pobierane asynchronicznie
        deliveryList.clear();
        deliveryList.addAll(getMockDeliveries());
        adapter.notifyDataSetChanged();

        updateEmptyView();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void refreshDeliveries() {
        // W rzeczywistej aplikacji, tutaj byłoby odświeżanie danych
        loadDeliveries();
    }

    private void updateEmptyView() {
        if (deliveryList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private List<Delivery> getMockDeliveries() {
        // Przykładowe dane dla demonstracji
        List<Delivery> mockList = new ArrayList<>();

        mockList.add(new Delivery("1", "PL123456789", "Jan Kowalski",
                "ul. Kwiatowa 1, Warszawa", "123456789", DeliveryStatus.NEW, new Date()));
        mockList.add(new Delivery("2", "PL987654321", "Anna Nowak",
                "ul. Polna 5, Kraków", "987654321", DeliveryStatus.IN_TRANSIT, new Date()));
        mockList.add(new Delivery("3", "PL543216789", "Piotr Wiśniewski",
                "ul. Długa 7, Wrocław", "567891234", DeliveryStatus.DELIVERED, new Date()));

        return mockList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delivery_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterDeliveries(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDeliveries(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter_all) {
            filterByStatus(null);
            return true;
        } else if (id == R.id.action_filter_new) {
            filterByStatus(DeliveryStatus.NEW);
            return true;
        } else if (id == R.id.action_filter_in_transit) {
            filterByStatus(DeliveryStatus.IN_TRANSIT);
            return true;
        } else if (id == R.id.action_filter_delivered) {
            filterByStatus(DeliveryStatus.DELIVERED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void filterDeliveries(String query) {
        if (query == null || query.isEmpty()) {
            adapter.updateList(deliveryList);
        } else {
            List<Delivery> filteredList = new ArrayList<>();
            String lowercaseQuery = query.toLowerCase();

            for (Delivery delivery : deliveryList) {
                if (delivery.getTrackingNumber().toLowerCase().contains(lowercaseQuery) ||
                        delivery.getRecipientName().toLowerCase().contains(lowercaseQuery) ||
                        delivery.getRecipientAddress().toLowerCase().contains(lowercaseQuery)) {
                    filteredList.add(delivery);
                }
            }

            adapter.updateList(filteredList);
        }

        updateEmptyView();
    }

    private void filterByStatus(DeliveryStatus status) {
        if (status == null) {
            adapter.updateList(deliveryList);
        } else {
            List<Delivery> filteredList = new ArrayList<>();

            for (Delivery delivery : deliveryList) {
                if (delivery.getStatus() == status) {
                    filteredList.add(delivery);
                }
            }

            adapter.updateList(filteredList);
        }

        updateEmptyView();
    }
}