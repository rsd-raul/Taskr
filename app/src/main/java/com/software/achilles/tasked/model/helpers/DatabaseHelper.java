package com.software.achilles.tasked.model.helpers;

import com.software.achilles.tasked.model.controllers.TaskController;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.repositiories.LabelRepository;
import com.software.achilles.tasked.model.repositiories.LocationRepository;
import com.software.achilles.tasked.model.repositiories.TaskListRepository;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmResults;

public class DatabaseHelper {

    public RealmResults<TaskList> findAllTaskList(){
        TaskListRepository taskListRepository = new TaskListRepository();

        return taskListRepository.findAll();
    }

    public RealmResults<Label> findAllLabels(){
        LabelRepository labelRepository = new LabelRepository();

        return labelRepository.findAll();
    }

    public RealmResults<Location> findAllLocations(){
        LocationRepository locationRepository = new LocationRepository();

        return locationRepository.findAll();
    }
}
