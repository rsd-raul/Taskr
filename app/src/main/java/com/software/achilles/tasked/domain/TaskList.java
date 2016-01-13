package com.software.achilles.tasked.domain;

import android.content.res.Resources;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.listeners.FloatingActionMenuConfigurator;
import java.io.Serializable;
import java.util.List;

public class TaskList extends BasicType implements Serializable {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private List<Task> tasks;

    // ------------------------- Constructor -------------------------

    public TaskList(int id, String title, List<Task> tasks) {
        setId(id);
        setTitle(title);
        this.tasks = tasks;
    }



    // ---------------------- Getters & Setters ----------------------


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
