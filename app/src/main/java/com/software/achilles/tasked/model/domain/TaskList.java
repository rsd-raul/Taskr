package com.software.achilles.tasked.model.domain;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.view.configurators.FloatingActionMenuConfigurator;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TaskList extends RealmObject implements BasicType{

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    @PrimaryKey
    private long id;
    private String title;
    private RealmList<Task> tasks;

    // ------------------------- Constructor -------------------------

    public TaskList() {
    }

    public TaskList(long id, String title, RealmList<Task> tasks) {
        this.id = id;
        this.title = title;
        this.tasks = tasks;
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
