package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Location extends RealmObject implements BasicType{

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private long id;
//    @Index        // Para lugares favoritos?
    @Required
    private String title;
    private String address;
    private double latitude;
    private double longitude;
    private boolean favourite;

    // ------------------------- Constructor -------------------------

    public Location() {
    }

    public Location(@NonNull String tit, String add, double lat, double lon, boolean fav){
        this.title = tit;
        this.address = add;
        this.latitude = lat;
        this.longitude = lon;
        this.favourite = fav;
    }

    // ---------------------- Getters & Setters ----------------------

    public long getId() {
        return id;
    }
    public void setId(long id) {
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
        return favourite;
    }
    public void setFavourite(boolean isFavourite) {
        this.favourite = isFavourite;
    }

    // -------------------------- To String --------------------------
}