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
import com.software.achilles.tasked.presenter.DashboardPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.RealmList;
import io.realm.RealmResults;

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

    public void firstTimePopulation(){
        TaskRepository taskRepository = new TaskRepository();
        LabelRepository labelRepository = new LabelRepository();
        TaskListRepository taskListRepository = new TaskListRepository();
        LocationRepository locationRepository = new LocationRepository();

        int amountList = 5;
        int amountTasks = 15;
        RealmList<TaskList> taskLists = new RealmList<>();


        // Fields to switch and use

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
            locationRepository.add(new Location(locationTitles[i], "", i, -i, true));
        RealmResults<Location> locations = locationRepository.findAll();

        for (int i = 0; i < labelTitles.length; i++)
            labelRepository.add(new Label(labelTitles[i], labelColors[i]));
        RealmResults<Label> labels = labelRepository.findAll();



        Random random = new Random();
        while (amountList > 0) {
            String listTitle = listTitles[random.nextInt(2)];
            taskListRepository.add(new TaskList(listTitle, null));

            RealmList<Task> aux = new RealmList<>();
            int amountTaskWhile = amountTasks;

            //TODO aqui nos quedamos, tenemos que popular para cada lista X tasks
//            while (amountTaskWhile > 0) {
//                Boolean finished = random.nextBoolean();
//                Boolean starred = random.nextBoolean();
//                String title = titles[random.nextInt(2)];
//                String description = descriptions[random.nextInt(3)];
//                Date dueDate = dueDates[random.nextInt(2)];
//                RealmList<Label> auxLabels = new RealmList<>();
//                Integer labelQuantity = labelQuantities[random.nextInt(4)];
//                Location location = locations.get(random.nextInt(3));
//
//                for (int i = 0; i < labelQuantity; i++)
//                    auxLabels.add(labels.get(random.nextInt(4)));
//
//                taskRepository.add(new Task(title, finished, starred, description, dueDate, location, auxLabels));
//
//                amountTaskWhile--;
//            }

//            String listTitle = listTitles[random.nextInt(2)];
//            TaskList taskList = new TaskList(listTitle, aux);
//            taskLists.add(taskList);
            amountList--;
        }



        Log.d("POPULATION: ", "COMPLETED");
        Log.d("TASKS: ", taskRepository.findAll() + "");
        Log.d("TASK_LISTS: ", taskListRepository.findAll() + "");
        Log.d("LOCATIONS: ", locationRepository.findAll() + "");
        Log.d("LABELS: ", labelRepository.findAll() + "");
//        Log.d("myApp", "CONTROLLER POPULATED" + " " +
//                taskLists.size() + " " +
//                taskLists.get(0).getTasks().size() + " " +
//                labels.size());
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
}
