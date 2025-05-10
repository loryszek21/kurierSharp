package com.example.kuriersharp.delivery.model;

public enum DeliveryStatus {
    NEW("Nowa"),
    IN_TRANSIT("W trakcie dostawy"),
    DELIVERED("Doręczona"),
    CANCELLED("Anulowana"),
    RETURNED("Zwrócona"),
    FAILED("Niedostarczona");

    private String displayName;

    DeliveryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}