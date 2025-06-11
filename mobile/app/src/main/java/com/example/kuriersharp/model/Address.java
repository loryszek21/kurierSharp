package com.example.kuriersharp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    public int id;
    public String street;
    public String city;
    public String region;
    public String postalCode;
    public String country;


    public Address(int id, String street, String city, String region, String postalCode, String country) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
    }
    protected Address(Parcel in) {
        id = in.readInt();
        street = in.readString();
        city = in.readString();
        region = in.readString();
        postalCode = in.readString();
        country = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(region);
        dest.writeString(postalCode);
        dest.writeString(country);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
