package com.software.achilles.tasked.domain;

import android.location.Location;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Task extends BasicType implements Serializable {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private Boolean finished;
    private String description;
    private Date dueDate;
    private Location location;
    ArrayList<Label> labels;

    // ------------------------- Constructor -------------------------

    public Task(Boolean finished, @NonNull String title, String description, Date dueDate, Location location, ArrayList<Label> labels) {
        this.finished = finished;
        setTitle(title);
        this.description = description;
        this.dueDate = dueDate;
        this.location = location;
        this.labels = labels;
    }

    // ---------------------- Getters & Setters ----------------------

    public Boolean getFinished() {
        return finished;
    }
    public void setFinished(Boolean finished) {
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

    public ArrayList<Label> getLabels() {
        return labels;
    }
    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    // -------------------------- To String --------------------------

    @Override
    public String toString() {
        return (finished ? "DONE - " : "") + getTitle() +
                (dueDate == null ? "" : " - " + dateToText(dueDate));
    }

    // ------------------------ Other methods ------------------------

    public static String dateToText(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.ENGLISH);
        return sdf.format(date);
    }
}
