package com.software.achilles.tasked.model.managers;

import com.software.achilles.tasked.model.controllers.TaskController;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.helpers.DatabaseHelper;

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


    // ------------------------- Constructor -------------------------
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
