package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import com.software.achilles.tasked.R;
import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Label extends RealmObject implements Serializable, BasicType {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private int id;
    private String title;
    private Integer colorRes;

    // ------------------------- Constructor -------------------------


    public Label() {
    }

    public Label(int id, @NonNull String title, Integer colorRes) {
        setId(id);
        setTitle(title);

        if(colorRes == null)
            this.colorRes = R.color.accent;
        else
            this.colorRes = colorRes;
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
        return getTitle();
    }

    // ------------------------ Other methods ------------------------

    @Override
    public boolean equals(Object o) {
        return o instanceof Label && ((Label) o).getTitle() == getTitle();
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}