package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Location extends RealmObject implements BasicType{

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private int id;
//    @Index        // Para lugares favoritos?
    @Required
    private String title;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isFavourite;

    // ------------------------- Constructor -------------------------

    public Location() {
    }

    public Location(int id, @NonNull String tit, String add, double lat, double lon, boolean isFav){
        this.id = id;
        this.title = tit;
        this.address = add;
        this.latitude = lat;
        this.longitude = lon;
        this.isFavourite = isFav;
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

    public boolean isFavourite() {
        return isFavourite;
    }
    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    // -------------------------- To String --------------------------
}
