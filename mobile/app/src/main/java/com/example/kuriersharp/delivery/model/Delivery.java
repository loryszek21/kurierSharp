package com.example.kuriersharp.delivery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Delivery implements Serializable {
    private String id;
    private String trackingNumber;
    private String recipientName;
    private String recipientAddress;
    private String recipientPhone;
    private DeliveryStatus status;
    private Date deliveryDate;
    private String notes;
    private List<String> photosPaths;
    private boolean isDelivered;
    private double latitude;
    private double longitude;

    public Delivery() {
        this.photosPaths = new ArrayList<>();
    }

    public Delivery(String id, String trackingNumber, String recipientName, String recipientAddress,
                    String recipientPhone, DeliveryStatus status, Date deliveryDate) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.recipientPhone = recipientPhone;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.photosPaths = new ArrayList<>();
        this.isDelivered = false;
    }

    // Gettery i settery
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getPhotosPaths() {
        return photosPaths;
    }

    public void setPhotosPaths(List<String> photosPaths) {
        this.photosPaths = photosPaths;
    }

    public void addPhotoPath(String path) {
        if (this.photosPaths == null) {
            this.photosPaths = new ArrayList<>();
        }
        this.photosPaths.add(path);
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}