package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Location extends RealmObject implements Serializable, BasicType{

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private int id;
//    @Index        // Para lugares favoritos?
    private String title;
    private String address;
    private Double latitude;
    private Double longitude;
    private boolean isFavourite;

    // ------------------------- Constructor -------------------------

    public Location() {
    }

    public Location(int id, @NonNull String title, String address, Double lat, Double lon, boolean isFavourite) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.latitude = lat;
        this.longitude = lon;
        this.isFavourite = isFavourite;
    }

    // ---------------------- Getters & Setters ----------------------

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isFavourite() {
        return isFavourite;
    }
    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    // -------------------------- To String --------------------------
}
