package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Task extends RealmObject implements BasicType {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private long id;
    @Index
    @Required
    private String title;
    private boolean completed, starred;
    private String notes;
    private Date due;
    private Location location;
    private RealmList<Label> labels;

    // ------------------------- Constructor -------------------------

    public Task() {
        this.labels = new RealmList<>();
    }

    // TODO no id in constructor
    public Task(@NonNull String title, boolean completed, boolean starred, String notes,
                Date due, Location location, RealmList<Label> labels) {
        this.completed = completed;
        this.starred = starred;
        this.title = title;
        this.notes = notes;
        this.due = due;
        this.location = location;
        this.labels = labels;
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

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDue() {
        return due;
    }
    public void setDue(Date due) {
        this.due = due;
    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    public RealmList<Label> getLabels() {
        return labels;
    }
    public void setLabels(RealmList<Label> labels) {
        this.labels = labels;
    }

    public boolean isStarred() {
        return starred;
    }
    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    // -------------------------- To String --------------------------

//    @Override
//    public String toString() {
//        // Access resources from Android in order to translate at sharing
//        Resources resources = FloatingActionMenuConfigurator.activity.getResources();
//        return (completed ? resources.getString(R.string.task_done) : "") + getTitle() +
//                (due == null ? "" : " - " + dateToText(due));
//    }

}
