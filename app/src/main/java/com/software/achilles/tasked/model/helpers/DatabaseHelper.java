package com.software.achilles.tasked.model.helpers;

import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.repositiories.LabelRepository;
import com.software.achilles.tasked.model.repositiories.LocationRepository;
import com.software.achilles.tasked.model.repositiories.TaskListRepository;
import com.software.achilles.tasked.model.repositiories.TaskRepository;
import com.software.achilles.tasked.util.Constants;

import io.realm.RealmList;
import io.realm.RealmResults;

public class DatabaseHelper {

    // ---------------------------- Find -----------------------------

    public RealmList<Task> findAllTasksByTaskListPosition(int position){
        TaskRepository taskRepository = new TaskRepository();

        return taskRepository.findAllByTaskListPosition(position);
    }

    public int findTaskListPositionById(long id){
        TaskListRepository taskListRepository = new TaskListRepository();

        RealmResults<TaskList> taskLists = taskListRepository.findAll();

        // Gives you the position of a TaskList based on its id
        for (int position = 0; position < taskLists.size(); position++)
            if(taskLists.get(position).getId() == id)
                return position;

        return -1;
    }

    public RealmResults<TaskList> findAllTaskList(){
        TaskListRepository taskListRepository = new TaskListRepository();

        return taskListRepository.findAll();
    }

    public TaskList findTaskListByPosition(int position){
        TaskListRepository taskListRepository = new TaskListRepository();

        return taskListRepository.findByPosition(position);
    }

    public RealmResults<Label> findAllLabels(){
        LabelRepository labelRepository = new LabelRepository();

        return labelRepository.findAll();
    }

    public RealmResults<Location> findAllLocations(){
        LocationRepository locationRepository = new LocationRepository();

        return locationRepository.findAll();
    }

    // ---------------------------- Save -----------------------------

    public void saveTask(Task task){
        TaskRepository taskRepository = new TaskRepository();

        taskRepository.save(task);
    }

    // --------------------------- Delete ----------------------------

    // ----------------------------- Get -----------------------------


    public void dashTaskModifier(int uniqueParameterId, Task task){

        new TaskRepository().taskModifier(uniqueParameterId, task);
    }

}
