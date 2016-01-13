package com.software.achilles.tasked.domain;

import android.support.annotation.NonNull;

import com.software.achilles.tasked.R;
import java.io.Serializable;

public class Label extends BasicType implements Serializable {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private Integer colorRes;

    // ------------------------- Constructor -------------------------

    public Label(int id, @NonNull String title, Integer colorRes) {
        setId(id);
        setTitle(title);

        if(colorRes == null)
            this.colorRes = R.color.accent;
    }

    // ---------------------- Getters & Setters ----------------------

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