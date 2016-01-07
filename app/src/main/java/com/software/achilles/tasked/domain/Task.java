package com.software.achilles.tasked.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task implements Serializable {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private int id;
    private String name, description;
    private Date dueDate;
    private Boolean finished;

    // ------------------------- Constructor -------------------------

    public Task(String name, String description, Date dueDate, Boolean finished) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.finished = finished;
    }

    // ---------------------- Getters & Setters ----------------------


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Boolean getFinished() {
        return finished;
    }
    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    // -------------------------- To String --------------------------

    @Override
    public String toString() {
        return (finished ? "DONE - " : "") + name +
                (dueDate == null ? "" : " - " + dateToText(dueDate));
    }

    // ------------------------ Other methods ------------------------

    public static String dateToText(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.ENGLISH);
        return sdf.format(date);
    }
}
