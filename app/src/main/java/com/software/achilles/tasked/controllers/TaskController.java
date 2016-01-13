package com.software.achilles.tasked.controllers;


import android.support.annotation.NonNull;
import android.util.Log;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.domain.Label;
import com.software.achilles.tasked.domain.Task;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.extras.DistributedRandomNumberGenerator;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TaskController {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private static TaskController instance;
    public static ArrayList<Task> sTasks;
    public static ArrayList<TaskList> sTaskLists;
    public static ArrayList<Label> sLabels;
    private Task actualTask, lastDeleted;
//    public static Date taskDate;

    public static Boolean addTaskOpen = false;

    // ------------------------- Constructor -------------------------

    public static TaskController getInstance() {
        if(instance == null)
            instance = new TaskController();
        return instance;
    }

    public TaskController() {
        if(sTasks == null)      //TODO ESTO HAY QUE QUITARLO
            sTasks = new ArrayList<>();

        randomPopulation(5, 15);    // TODO SOLO PARA TEST
    }

    // TODO SOLO PARA TEST
    private void randomPopulation(int amountList, int amountTasks) {
        sTaskLists = new ArrayList<>();
        sLabels = new ArrayList<>();

        // Fields to switch and use

        String[] titles = new String[]{"Tarea increiblemente larga", "Tarea corta"};
        String[] descriptions = new String[]{"Descripcion increiblemente larga", "Descripcion corta", null};
        Date[] dueDates = new Date[]{Calendar.getInstance().getTime(), null};

        String[] listTitles = new String[]{"Lista muy larga", "Lista corta"};

        String[] labelTitles = new String[]{"Compras", "Trabajo", "Coche", "Perro", "Random", "Raaaaaandom"};
        Integer[] labelColors = new Integer[]{R.color.amberDate, R.color.colorPrimary, R.color.tealLocation, null};
        Integer[] labelQuantities = new Integer[]{0, 1, 2, 3};

        for (int i = 0; i < 6; i++) {
            Integer labelColor = (i < 3) ? labelColors[i] : null;

            sLabels.add(new Label(i+600, labelTitles[i], labelColor));
        }

        Random random = new Random();
        while (amountList > 0) {
            List<Task> aux = new ArrayList<>();
            int amountTaskWhile = amountTasks;

            while (amountTaskWhile > 0) {
                Boolean finished = (random.nextBoolean());
                String title = titles[random.nextInt(2)];
                String description = descriptions[random.nextInt(3)];
                Date dueDate = dueDates[random.nextInt(2)];
                Set<Label> labels = new HashSet<>();
                Integer labelQuantity = labelQuantities[random.nextInt(4)];

                for (int i = 0; i < labelQuantity; i++)
                    labels.add(sLabels.get(random.nextInt(6)));

                aux.add(new Task(finished, title, description, dueDate, null, new ArrayList<>(labels)));
                amountTaskWhile--;
            }

            String listTitle = listTitles[random.nextInt(2)];
            TaskList taskList = new TaskList(amountList+300, listTitle, aux);
            sTaskLists.add(taskList);
            amountList--;
        }

        Log.d("myApp", "CONTROLLER POPULATED" + " " +
                sTaskLists.size() + " " +
                sTaskLists.get(0).getTasks().size() + " " +
                sLabels.size());
    }

    // TODO SOLO PARA TEST... O NO.
    public static int getPositionById(int id){
        // Gives you the position of a TaskList based on its id
        for (int positionOnList = 0; positionOnList < sTaskLists.size(); positionOnList++)
            if(sTaskLists.get(positionOnList).getId() == id)
                return positionOnList;

        return -1;
    }

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
        for (Task task : sTasks) {
            Date dueDate = task.getDueDate();
            if(dueDate!=null)
                if (!task.getFinished() && (dueDate.after(min) && dueDate.before(max) || dueDate.equals(max)))
                    counter.add(task);
        }

        return counter;
    }

    // ---------------------- Getters & Setters ----------------------

    public ArrayList<Task> getTasks(){
        return sTasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        TaskController.sTasks = tasks;
    }

    public void setLastDeleted(Task lastDeleted) {
        this.lastDeleted = lastDeleted;
    }

}
