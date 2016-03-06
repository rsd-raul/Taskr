package com.software.achilles.tasked.model.managers;

import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.helpers.DatabaseHelper;
import com.software.achilles.tasked.presenter.DashboardPresenter;

import java.util.ArrayList;

public class DataManager {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private static DataManager instance;

    private DatabaseHelper mDatabaseHelper;

    public static DataManager getInstance() {
        if(instance == null)
            instance = new DataManager();
        return instance;
    }

    public DataManager() {
        this.mDatabaseHelper = new DatabaseHelper();
    }

    public ArrayList<TaskList> findAllTaskList(){
        return mDatabaseHelper.findAllTaskList();
    }

    public int getTaskListPositionById(int id){
        // Get the current task lists
        ArrayList<TaskList> taskLists = mDatabaseHelper.findAllTaskList();

        // Gives you the position of a TaskList based on its id
        for (int position = 0; position < taskLists.size(); position++)
            if(taskLists.get(position).getId() == id)
                return position;

        return -1;
    }

    // -------------------------- Filtering --------------------------

    int size = 0;

    public ArrayList<Task> filterByText(String query, Boolean searchDeep){
        ArrayList<Task> result = size < query.length() ? getCurrentTasks() : getAllTasks();
        size = query.length();

        return searchDeep ? deepSearch(result) : lightSearch(result);
    }

    public ArrayList<Task> deepSearch(ArrayList<Task> tasks){
        // Busqueda final, aprovecha lo que tienes
        return new ArrayList<>();
    }

    public ArrayList<Task> lightSearch(ArrayList<Task> tasks){
        // Tengo que lanzar un hilo y cancelarlo si el usuario teclea otra letra, cosa que si estoy
        // buscando "hola" no se tire 20 segundos en buscar todas las tareas que tienen una "h"
        return new ArrayList<>();
    }

    public ArrayList<Task> filterByFilterDrawerMoreValues(){

        return getAllTasks();
    }

    public ArrayList<Task> filterByFilterDrawerLessValues(){
//        return mDatabaseHelper.filterTaskListBy(index, values);
        return new ArrayList<>();
    }

    public ArrayList<Task> filterByFilterDrawer(){
        return new ArrayList<>();
    }

    // Retrieve the current Tasks in the ViewPager (take advantage of a previous filter)
    public ArrayList<Task> getAllTasks(){
//        return mDatabaseHelper.getAllTaksForTaskList(index);
        return new ArrayList<>();
    }

    // Get all tasks from the Database in order to filter again (filter less restrictive now)
    public ArrayList<Task> getCurrentTasks(){
//        ArrayList<Task> result = new ArrayList<>();
//        result.addAll( mDashboardPresenter.getTasksInCurrentPage() );
        return new ArrayList<>();
    }

    // ------------------------- View Holder -------------------------
    // -------------------------- Landscape --------------------------
    // ---------------------------- Menu -----------------------------
    // -------------------------- Use Cases --------------------------
    // -------------------------- Interface --------------------------
    // --------------------- Add Task Interface ----------------------
    // --------------------------- Details ---------------------------
    // ------------------------ Notifications ------------------------
    // ------------------------- Preferences -------------------------
    // -------------------------- FAB child --------------------------
    // -------------------------- FAB menu ---------------------------
}
