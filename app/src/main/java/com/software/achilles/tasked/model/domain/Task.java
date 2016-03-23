package com.software.achilles.tasked.model.domain;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.view.configurators.FloatingActionMenuConfigurator;

import java.text.DateFormat;
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
    private int id;
    @Index
    @Required
    private String title;
    private boolean finished, starred;
    private String description;
    private Date dueDate;
    private Location location;
    private RealmList<Label> labels;

    // ------------------------- Constructor -------------------------

    public Task() {
    }

    // TODO no id in constructor
    public Task(boolean finished, boolean starred, @NonNull String title, String description, Date dueDate, Location location, RealmList<Label> labels) {
        this.finished = finished;
        this.starred = starred;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.location = location;
        this.labels = labels;
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

    public boolean getFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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

    public boolean getStarred() {
        return starred;
    }
    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    // -------------------------- To String --------------------------

    @Override
    public String toString() {
        // Access resources from Android in order to translate at sharing
        Resources resources = FloatingActionMenuConfigurator.activity.getResources();
        return (finished ? resources.getString(R.string.task_done) : "") + getTitle() +
                (dueDate == null ? "" : " - " + dateToText(dueDate));
    }

    // ------------------------ Other methods ------------------------

    public static String dateToText(Date date){
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        return df.format(date);
    }
}
