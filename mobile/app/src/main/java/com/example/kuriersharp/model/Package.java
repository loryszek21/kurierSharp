package com.example.kuriersharp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Package {
    public int id;
    public String trackingNumber;
    public double weightKg;
    public int status;
    public Date createdAt;
    public Date deliveredAt;
    public Address address;
    public int senderId;
    public Person sender;
    public int recipientId;
    public Person recipient;

    public Package(int id, String trackingNumber, double weightKg, int status, Date createdAt, Date deliveredAt, Address address, int senderId, Person sender, int recipientId, Person recipient) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.weightKg = weightKg;
        this.status = status;
        this.createdAt = createdAt;
        this.deliveredAt = deliveredAt;
        this.address = address;
        this.senderId = senderId;
        this.sender = sender;
        this.recipientId = recipientId;
        this.recipient = recipient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Date deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    //    public Package(JSONObject jsonObject) throws JSONException {
//        this.id = jsonObject.getInt("id");
//        this.trackingNumber = jsonObject.getString("trackingNumber");
//        this.weightKg = jsonObject.getDouble("weightKg");
//        this.status = jsonObject.getInt("status");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
//        this.createdAt = dateFormat.parse(jsonObject.getString("createdAt"));
//        this.deliveredAt = dateFormat.parse(jsonObject.getString("deliveredAt"));
//        this.address = new Address(jsonObject.getJSONObject("address")); // zakładając, że Address ma podobny konstruktor
//        this.senderId = jsonObject.getInt("senderId");
//        this.sender = new Person(jsonObject.getJSONObject("sender")); // zakładając, że Person ma podobny konstruktor
//        this.recipientId = jsonObject.getInt("recipientId");
//        this.recipient = new Person(jsonObject.getJSONObject("recipient")); // zakładając, że Person ma podobny konstruktor
//    }
}
