package com.software.achilles.tasked.model.domain;

import android.support.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TaskList extends RealmObject implements BasicType{

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private long id;
    @Required
    private String title;
    private RealmList<Task> tasks;

    // ------------------------- Constructor -------------------------

    public TaskList() {
    }

    public TaskList(@NonNull String title, RealmList<Task> tasks) {
        this.title = title;
        this.tasks = tasks != null ? tasks : new RealmList<Task>();
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

    public RealmList<Task> getTasks() {
        return tasks;
    }
    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }

    // ------------------------ Other methods ------------------------
}
