package com.software.achilles.tasked.model.managers;

import android.util.Log;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.repositories.LabelRepository;
import com.software.achilles.tasked.model.repositories.LocationRepository;
import com.software.achilles.tasked.model.repositories.TaskListRepository;
import com.software.achilles.tasked.model.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.realm.RealmList;
import io.realm.RealmResults;

@Singleton
public class DataManager {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    // -------------------------- Injected ---------------------------

    LabelRepository labelRepository;
    LocationRepository locationRepository;
    TaskListRepository taskListRepository;
    TaskRepository taskRepository;

    // ------------------------ Constructor --------------------------

    @Inject
    public DataManager(LabelRepository labelRepository, LocationRepository locationRepository,
                       TaskListRepository taskListRepository, TaskRepository taskRepository) {
        this.labelRepository = labelRepository;
        this.locationRepository = locationRepository;
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    // ---------------------------- Find -----------------------------

    public RealmResults<TaskList> findAllTaskList(){
        return taskListRepository.findAll();
    }


    public Task findTaskById(long id){
        return taskRepository.findOne(id);
    }

    public RealmList<Task> findAllTasksByTaskListPosition(int position){
        return taskRepository.findAllByTaskListPosition(position);
    }

    public TaskList findTaskListByPosition(int position){
        return taskListRepository.findByPosition(position);
    }

    public RealmResults<Label> findAllLabels(){
        return labelRepository.findAll();
    }

    public RealmResults<Location> findAllLocations(){
        return locationRepository.findAll();
    }

    public int findTaskListPositionById(int id){
        RealmResults<TaskList> taskLists = taskListRepository.findAll();

        // Gives you the position of a TaskList based on its id
        for (int position = 0; position < taskLists.size(); position++)
            if(taskLists.get(position).getId() == id)
                return position;

        return -1;
    }

    // ---------------------------- Save -----------------------------

    public void saveTask(Task task){
        taskRepository.save(task);

        // If Create, save on the new list
        if(oldTaskList == null){
            taskListRepository.addTaskToTaskList(task);
            return;
        }

        // If Edit and the list is different, remove from one, add to the other
        if(task.getTaskList().getId() != oldTaskList.getId()){
            taskListRepository.addTaskToTaskList(task);
            taskListRepository.removeTaskFromTaskList(oldTaskList.getId(), task.getId());
        }
    }

    public void saveTaskList(TaskList taskList){
        taskListRepository.save(taskList);
    }

    public void saveLabel(Label label){
        labelRepository.save(label);
    }

    // --------------------------- Delete ----------------------------

    // ----------------------------- Get -----------------------------

    // ------------------------ Temporal Task ------------------------

    private Task temporalTask;
    private int temporalTaskListPosition;
    private TaskList oldTaskList;

    public Task getTemporalTask() {
        temporalTask = (temporalTask != null) ? temporalTask : new Task();
        return temporalTask;
    }
    public void setTemporalTask(Task temporalTask) {
        this.temporalTask = temporalTask;
    }

    public void destroyTemporalTask() {
        temporalTask = null;
    }

    public int getTemporalTaskListPosition(){
        return temporalTaskListPosition;
    }
    public void setTemporalTaskListPosition(int temporalTaskListPosition){
        this.temporalTaskListPosition = temporalTaskListPosition;
    }

    public void setOldTaskList(TaskList oldTaskList) {
        this.oldTaskList = oldTaskList;
    }

    // -------------------------- Use Cases --------------------------

    public void dashTaskModifier(int uniqueParameterId, Task task, Date date){
        taskRepository.taskModifier(uniqueParameterId, task, date);
    }




// ------------------------ NOT TESTED ------------------------ NOT TESTED ------------------------

    // -------------------------- Filtering --------------------------

//    int size = 0;

    public RealmResults<Task> filterByText(String query, Boolean searchDeep){
        return taskRepository.findAllByTitle(query);
    }

//    public ArrayList<Task> filterByText(String query, Boolean searchDeep){
//        ArrayList<Task> result = size < query.length() ? getCurrentTasks() : getAllTasks();
//        size = query.length();
//
//        return searchDeep ? deepSearch(result) : lightSearch(result);
//    }

//    public ArrayList<Task> deepSearch(ArrayList<Task> tasks){
//        // Busqueda final, aprovecha lo que tienes
//        return new ArrayList<>();
//    }

//    public ArrayList<Task> lightSearch(ArrayList<Task> tasks){
//        // Tengo que lanzar un hilo y cancelarlo si el usuario teclea otra letra, cosa que si estoy
//        // buscando "hola" no se tire 20 segundos en buscar todas las tareas que tienen una "h"
//        return new ArrayList<>();
//    }

//    public ArrayList<Task> filterByFilterDrawerMoreValues(){
//
//        return getAllTasks();
//    }

//    public ArrayList<Task> filterByFilterDrawerLessValues(){
////        return mDatabaseHelper.filterTaskListBy(index, values);
//        return new ArrayList<>();
//    }

//    public ArrayList<Task> filterByFilterDrawer(){
//        return new ArrayList<>();
//    }

//    // Retrieve the current Tasks in the ViewPager (take advantage of a previous filter)
//    public ArrayList<Task> getAllTasks(){
////        return mDatabaseHelper.getAllTaskForTaskList(index);
//        return new ArrayList<>();
//    }

//    // Get all tasks from the Database in order to filter again (filter less restrictive now)
//    public ArrayList<Task> getCurrentTasks(){
////        ArrayList<Task> result = new ArrayList<>();
////        result.addAll( mDashboardPresenter.getTasksInCurrentPage() );
//        return new ArrayList<>();
//    }

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

    // REVIEW - Helper - Remove everything and include "tips and tricks" tasks
    // If the user doesn't have any labels, add 3/4 by default
    // If user doesn't have locations, add "Home" and "Work" by default and ask to initialize.
    public void firstTimePopulation(){
        int amountList = 5;
        int amountTasks = 15;

        Log.d("POPULATION: ", "STARTED");

        // Fields to switch and use randomly
        String[] titles = new String[]{"Unbelievable long task", "Short task"};
        String[] descriptions = new String[]{"Unbelievable long description, like, really long",
                "Short description", null};
        Date[] dueDates = new Date[]{Calendar.getInstance().getTime(), null};
        String[] listTitles = new String[]{"Really long list", "Short list"};

        String[] locationTitles = new String[]{"Home", "Work", "Market", "Gym"};

        String[] labelTitles = new String[]{"Groceries", "Inspiration", "Personal", "Work"};
        Integer[] labelColors = new Integer[]{R.color.amberDate, R.color.colorPrimary,
                R.color.tealLocation, R.color.app_body_text_1};
        Integer[] labelQuantities = new Integer[]{0, 1, 2, 3};

        for (int i = 0; i < locationTitles.length; i++)
            locationRepository.save(new Location(locationTitles[i], "", i, -i, true));
        RealmResults<Location> locations = locationRepository.findAll();

        Log.d("LOCATIONS: ", locationRepository.findAll() + "");

        for (int i = 0; i < labelTitles.length; i++)
            labelRepository.save(new Label(labelTitles[i], labelColors[i]));
        RealmResults<Label> labels = labelRepository.findAll();

        Log.d("LABELS: ", labelRepository.findAll() + "");

        Random random = new Random();
        while (amountList > 0) {
            String listTitle = listTitles[random.nextInt(2)];
            taskListRepository.save(new TaskList(listTitle, null));
            TaskList realmList = taskListRepository.findByPosition(5-amountList);


            int amountTaskWhile = amountTasks;

            while (amountTaskWhile > 0) {
                Boolean finished = random.nextBoolean();
                Boolean starred = random.nextBoolean();
                String title = titles[random.nextInt(2)];
                String description = descriptions[random.nextInt(3)];
                Date dueDate = dueDates[random.nextInt(2)];
                RealmList<Label> auxLabels = new RealmList<>();
                Integer labelQuantity = labelQuantities[random.nextInt(4)];
                Location location = locations.get(random.nextInt(3));

                // Add random labels to the task we are creating - No duplicates
                List<Integer> usedLabels = new ArrayList<>();
                for (int i = 0; i < labelQuantity; i++) {
                    Integer index;
                    do{
                        index = random.nextInt(4);
                    }while (usedLabels.contains(index));
                    usedLabels.add(index);
                    auxLabels.add(labels.get(index));
                }

                Task task = new Task(title, realmList, finished, starred, description, dueDate, location, auxLabels);
                saveTask(task);

                amountTaskWhile--;
            }
            amountList--;
        }

        Log.d("TASKS: ", taskRepository.findAll() + "");
        Log.d("TASK_LISTS: ", taskListRepository.findAll() + "");

        Log.d("POPULATION: ", "COMPLETED");
    }
}
