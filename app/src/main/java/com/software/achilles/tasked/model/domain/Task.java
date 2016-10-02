package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Task extends RealmObject implements BasicType {

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private long id;
    @Index
    @Required
    private String title;
    private boolean completed, starred;
    @Index
    private String notes;
    private Date due;
    private Location location;
    private RealmList<Label> labels;
    private TaskList taskList;

    // ------------------------- Constructor -------------------------

    public Task() {
        this.labels = new RealmList<>();
    }

    // TODO no id in constructor
    public Task(@NonNull String title, @NonNull TaskList taskList, boolean completed,
                boolean starred, String notes, Date due, Location location,
                RealmList<Label> labels) {
        this.completed = completed;
        this.starred = starred;
        this.title = title;
        this.notes = notes;
        this.due = due;
        this.location = location;
        this.labels = labels;
        this.taskList = taskList;
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

    public TaskList getTaskList() {
        return taskList;
    }
    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
}
