package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Location extends RealmObject implements BasicType{

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private long id;
    @Required
    private String title;
    private String address;
    private double latitude;
    private double longitude;
    private Double southwest_lat, southwest_lon, northeast_lat, northeast_lon;
    private boolean favourite;

    // ------------------------- Constructor -------------------------

    public Location() {
    }

    public Location(@NonNull String tit, String add, double lat, double lon, double[] bounds, boolean fav){
        this.title = tit;
        this.address = add;
        this.latitude = lat;
        this.longitude = lon;
        this.favourite = fav;
        setBounds(bounds);
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

    public double[] getBounds() {
        if(southwest_lat == null)
            return null;
        return new double[]{southwest_lat, southwest_lon, northeast_lat, northeast_lon};
    }
    public void setBounds(double[] bounds) {
        if(bounds != null) {
            this.southwest_lat = bounds[0];
            this.southwest_lon = bounds[1];
            this.northeast_lat = bounds[2];
            this.northeast_lon = bounds[3];
        }else{
            this.southwest_lat = null;
            this.southwest_lon = null;
            this.northeast_lat = null;
            this.northeast_lon = null;
        }
    }
}
