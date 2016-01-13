package com.software.achilles.tasked.domain;

import android.support.annotation.NonNull;

import com.software.achilles.tasked.R;
import java.io.Serializable;

public class Label implements Serializable {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private int id;
    private String title;
    private Integer colorRes;

    // ------------------------- Constructor -------------------------

    public Label(int id, @NonNull String title, Integer colorRes) {
        this.id = id;
        this.title = title;

        if(colorRes == null)
            this.colorRes = R.color.accent;
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

    public int getColorRes() {
        return colorRes;
    }
    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }

// -------------------------- To String --------------------------

    @Override
    public String toString() {
        return title;
    }

    // ------------------------ Other methods ------------------------

    @Override
    public boolean equals(Object o) {
        return o instanceof Label && ((Label) o).getTitle() == this.title;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}