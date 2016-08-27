package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import com.software.achilles.tasked.R;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Label extends RealmObject implements BasicType {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private long id;
    @Required
    private String title;
    private int colorRes;

    // ------------------------- Constructor -------------------------


    public Label() {
    }

    public Label(@NonNull String title, int colorRes) {
        this.title = title;
        // If the user doesn't select a color, the label will be "colorAccent"
        this.colorRes = colorRes == -1 ? R.color.colorAccent : colorRes;
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

    public int getColorRes() {
        return colorRes;
    }
    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }

// -------------------------- To String --------------------------

    @Override
    public String toString() {
        return getTitle();
    }

    // ------------------------ Other methods ------------------------

    @Override
    public boolean equals(Object o) {
        return o instanceof Label && ((Label) o).getTitle().equals(getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}