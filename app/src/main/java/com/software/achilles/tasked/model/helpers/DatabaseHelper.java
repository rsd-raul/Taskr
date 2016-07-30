package com.software.achilles.tasked.model.helpers;

import com.software.achilles.tasked.model.domain.Task;

public abstract class DatabaseHelper {

    public static Task removeRealmFromTask(Task task){
        Task result = new Task();

        result.setLabels(task.getLabels());
        result.setNotes(task.getNotes());
        result.setTitle(task.getTitle());
        result.setStarred(task.isStarred());
        result.setCompleted(task.isCompleted());
        result.setId(task.getId());
        result.setLocation(task.getLocation());
        result.setDue(task.getDue());

        return result;
    }
}
