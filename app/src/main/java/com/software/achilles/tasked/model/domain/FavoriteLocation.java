package com.software.achilles.tasked.model.domain;

import android.location.Location;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class FavoriteLocation extends BasicType implements Serializable{

    private Location location;

    public FavoriteLocation(int id, @NonNull String title, Location location) {
        setId(id);
        setTitle(title);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
}
