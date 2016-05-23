package com.software.achilles.tasked.model.managers;

import android.util.Log;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.helpers.DatabaseHelper;
import com.software.achilles.tasked.model.repositiories.LabelRepository;
import com.software.achilles.tasked.model.repositiories.LocationRepository;
import com.software.achilles.tasked.model.repositiories.TaskListRepository;
import com.software.achilles.tasked.model.repositiories.TaskRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.RealmList;
import io.realm.RealmResults;

public class DataManager {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private DatabaseHelper mDatabaseHelper;

    // -------------------------- Singleton --------------------------

//  Correct lazy initialization in Java - TODO - FINAL TESTING - Test in case of better performance
//
//    // Nested classes are not loaded until they are referenced.
//    private static class DataManagerHolder {
//        public static final DataManager instance = new DataManager();
//    }

//
//    public static DataManager getInstance() {
//        return DataManagerHolder.instance;
//    }

//  Double-checked locking - Effective in Java 1.5 and later:
    private static final Object lock = new Object();
    private static volatile DataManager instance;

    public static DataManager getInstance() {
        DataManager result = instance;

        // Only synchronize if the DataManager haven't been instantiated
        if (result == null) {
            synchronized (lock) {
                result = instance;

                // If no other threads have instantiated the DataManager while waiting for the lock.
                if (result == null) {
                    result = new DataManager();
                    instance = result;
                }
            }
        }
        return result;
    }

    private DataManager() {
        this.mDatabaseHelper = new DatabaseHelper();
    }

    // ---------------------------- Find -----------------------------

    public RealmResults<TaskList> findAllTaskList(){
        return mDatabaseHelper.findAllTaskList();
    }

    public RealmList<Task> findAllTasksByTaskListPosition(int position){
        return mDatabaseHelper.findAllTasksByTaskListPosition(position);
    }

    public TaskList findTaskListByPosition(int position){
        return mDatabaseHelper.findTaskListByPosition(position);
    }

    public RealmResults<Label> findAllLabels(){
        return mDatabaseHelper.findAllLabels();
    }

    public RealmResults<Location> findAllLocations(){
        return mDatabaseHelper.findAllLocations();
    }

    public Label findLastLabel(){
        return mDatabaseHelper.findLastLabel();
    }


    // ---------------------------- Save -----------------------------

    public void saveTask(Task task){
        mDatabaseHelper.saveTask(task);
    }

    public void saveTaskList(TaskList taskList){
        mDatabaseHelper.saveTaskList(taskList);
    }

    public void saveLabel(Label label){
        mDatabaseHelper.saveLabel(label);
    }

    // --------------------------- Delete ----------------------------

    // ----------------------------- Get -----------------------------

    public int getTaskListPositionById(int id){
        return mDatabaseHelper.findTaskListPositionById(id);
    }

    // -------------------------- Use Cases --------------------------

    public void dashTaskModifier(int uniqueParameterId, Task task){
        mDatabaseHelper.dashTaskModifier(uniqueParameterId, task);
    }




// ------------------------ NOT TESTED ------------------------ NOT TESTED ------------------------

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
//        return mDatabaseHelper.getAllTaskForTaskList(index);
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

    // TODO Remove everything and include "tips and tricks" tasks
    // If the user doesn't have any labels, add 3/4 by default
    // If user doesn't have locations, add "Home" and "Work" by default and ask to initialize.
    public void firstTimePopulation(){
        int amountList = 5;
        int amountTasks = 15;

        Log.d("POPULATION: ", "STARTED");

        TaskRepository taskRepository = new TaskRepository();
        LabelRepository labelRepository = new LabelRepository();
        TaskListRepository taskListRepository = new TaskListRepository();
        LocationRepository locationRepository = new LocationRepository();

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
            TaskList taskList = mDatabaseHelper.findLastTaskList();

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

                for (int i = 0; i < labelQuantity; i++)
                    auxLabels.add(labels.get(random.nextInt(4)));

                Task task = new Task(title, finished, starred, description, dueDate, location, auxLabels);
                taskRepository.save(task);
                taskListRepository.addTaskToTaskList(taskList.getId(), task);

                amountTaskWhile--;
            }
            amountList--;
        }

        Log.d("TASKS: ", taskRepository.findAll() + "");
        Log.d("TASK_LISTS: ", taskListRepository.findAll() + "");

        Log.d("POPULATION: ", "COMPLETED");
    }
}
