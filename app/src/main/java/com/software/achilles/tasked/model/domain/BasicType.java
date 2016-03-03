package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class BasicType implements Serializable{

    // --------------------------- Values ----------------------------

    private int id;
    private String title;

    // ------------------------- Attributes --------------------------

    // ------------------------- Constructor -------------------------

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

    // -------------------------- Landscape --------------------------
    // ---------------------------- Menu -----------------------------
    // -------------------------- Use Cases --------------------------
    // -------------------------- Interface --------------------------
    // --------------------- Add Task Interface ----------------------
    // --------------------------- Details ---------------------------
    // ------------------------ Notifications ------------------------
    // ------------------------- Preferences -------------------------
    // -------------------------- FAB child --------------------------
    // -------------------------- FAB menu ---------------------------
}
