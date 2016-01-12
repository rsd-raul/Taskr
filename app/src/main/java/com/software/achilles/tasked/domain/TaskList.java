package com.software.achilles.tasked.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskList implements Serializable {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private int id;
    private String title;
    private List<Task> tasks;

    // ------------------------- Constructor -------------------------

    public TaskList(int id, String title, List<Task> tasks) {
        this.id = id;
        this.title = title;
        this.tasks = tasks;
    }

    public TaskList(String title, List<Task> tasks) {
        this.title = title;
        this.tasks = tasks;
    }

    public TaskList(String title) {
        this.title = title;
        this.tasks = new ArrayList<>();
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
    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // -------------------------- To String --------------------------

    @Override
    public String toString() {
        return super.toString();
    }

    // ------------------------ Other methods ------------------------
}
