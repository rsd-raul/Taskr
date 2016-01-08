package com.software.achilles.tasked.controllers;


import android.support.annotation.NonNull;

import com.software.achilles.tasked.domain.Task;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskController {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private static TaskController instance;
    public static ArrayList<Task> tasks;
    private Task actualTask, lastDeleted;
//    public static Date taskDate;

    public static Boolean addTaskOpen = false;

    // ------------------------- Constructor -------------------------

    public static TaskController getInstance() {
        if(instance == null)
            instance = new TaskController();
        return instance;
    }

    public TaskController() {    }

    // ------------------------ Crud Methods -------------------------

    public void addTask(@NonNull Task newTask){

        // Add on top
        getTasks().add(0, newTask);
    }

    public void editTask(int position, @NonNull Task task){

        // Validation of the task title
        if(!task.getTitle().matches(""))
            getTasks().set(position, task);
    }

    public void deleteTask(int position){
        getTasks().remove(position);
    }

    public void undoDelete(){

        // Adds the last deleted to the top
        if(lastDeleted != null)
            getTasks().add(0, lastDeleted);
    }

    public void finishTask(int position){
        actualTask = getTasks().get(position);

        actualTask.setFinished(true);
        getTasks().set(position, actualTask);
    }

    public void undoFinished(int position){
        actualTask = getTasks().get(position);

        actualTask.setFinished(false);
        getTasks().set(position, actualTask);
    }

    // -------------------------- Use Cases --------------------------

    public static List<Task> getTasksOnRange(String range, boolean today, boolean last){

        Date min = Calendar.getInstance().getTime();
        min.setSeconds(0);
        Date max = new Date();

        switch (range){
            case "Daily":
                max.setTime(min.getTime() + Constants.DAY_IN_MILLISECOND);
                break;
            case "Weekly":
                max.setTime(min.getTime() + Constants.WEEK_IN_MILLISECOND);
                break;
            case "Minute":
                max.setTime(min.getTime() + Constants.MINUTE_IN_MILLISECOND);
                break;
        }

        // Include today inside the range if requested
        if(today){
            min.setMinutes(0);
            min.setHours(0);
        }

        // Include the las day of the range if requested
        if(last){
            max.setHours(23);
            max.setMinutes(59);
            max.setSeconds(59);
        }

        // Filter the tasks depending on the range
        List<Task> counter = new ArrayList<>();
        for (Task task : tasks) {
            Date dueDate = task.getDueDate();
            if(dueDate!=null)
                if (!task.getFinished() && (dueDate.after(min) && dueDate.before(max) || dueDate.equals(max)))
                    counter.add(task);
        }

        return counter;
    }

    // ---------------------- Getters & Setters ----------------------

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        TaskController.tasks = tasks;
    }

    public void setLastDeleted(Task lastDeleted) {
        this.lastDeleted = lastDeleted;
    }

}
