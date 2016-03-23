package com.software.achilles.tasked.model.repositiories;

import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {Label.class, Task.class, TaskList.class, Location.class})
public class SimpleRealmModule {
}
