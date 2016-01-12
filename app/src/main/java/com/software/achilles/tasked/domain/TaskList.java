package com.software.achilles.tasked.domain;

import android.content.res.Resources;

import com.software.achilles.tasked.MainActivity;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.listeners.FloatingActionMenuConfigurator;

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

        // Access resources from Android in order to translate at sharing
        Resources resources = FloatingActionMenuConfigurator.activity.getResources();
        String result = resources.getString(R.string.listName) + " " + getTitle() + "\n\n";
        result += resources.getString(R.string.tasks);

        // This is supposed to be more efficient that an extended for
        List<Task> tasks = getTasks();
        for (int i = 0; i < tasks.size(); i++)
            result += "\n" + tasks.get(i).toString();

        return result;
    }

    // ------------------------ Other methods ------------------------
}
