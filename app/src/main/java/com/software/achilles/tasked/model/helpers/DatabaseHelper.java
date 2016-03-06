package com.software.achilles.tasked.model.helpers;

import com.software.achilles.tasked.model.controllers.TaskController;
import com.software.achilles.tasked.model.domain.TaskList;

import java.util.ArrayList;

public class DatabaseHelper {

    public ArrayList<TaskList> findAllTaskList(){
        return TaskController.sTaskLists;
    }
}
