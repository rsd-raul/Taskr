//package com.software.achilles.tasked.model.controllers;
//
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.software.achilles.tasked.R;
//import com.software.achilles.tasked.model.domain.Location;
//import com.software.achilles.tasked.model.domain.Label;
//import com.software.achilles.tasked.model.domain.Task;
//import com.software.achilles.tasked.model.domain.TaskList;
//import com.software.achilles.tasked.util.Constants;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//import io.realm.RealmList;
//
//public class TaskController {
//
//    // --------------------------- Values ----------------------------
//
//    // ------------------------- Attributes --------------------------
//
//    private static TaskController instance;
////    public static ArrayList<Task> sTasks;
////    public static ArrayList<TaskList> sTaskLists;
////    public static ArrayList<Label> sLabels;
////    public static ArrayList<Location> sFavouriteLocations;
//    private Task actualTask, lastDeleted;
////    public static Date taskDate;
//
//    public static Boolean addTaskOpen = false;
//
//    // ------------------------- Constructor -------------------------
//
//    public static TaskController getInstance() {
//        if(instance == null)
//            instance = new TaskController();
//        return instance;
//    }
//
//    private TaskController() {
////        if(sTasks == null)      //TODO ESTO HAY QUE QUITARLO
////            sTasks = new ArrayList<>();
//
//        // TODO thread
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                randomPopulation(5, 15);    // TODO SOLO PARA TEST
////            }
////        }).start();
//    }
//
////    // TODO SOLO PARA TEST
////    private void randomPopulation(int amountList, int amountTasks) {
////        sTaskLists = new ArrayList<>();
////        sLabels = new ArrayList<>();
////        sFavouriteLocations = new ArrayList<>();
////
////        // Fields to switch and use
////
////        String[] titles = new String[]{"Unbelievable long task", "Short task"};
////        String[] descriptions = new String[]{"Un", "Descripcion corta", null};
////        Date[] dueDates = new Date[]{Calendar.getInstance().getTime(), null};
////
////        String[] listTitles = new String[]{"Really long list", "Short list"};
////
////        String[] locationTitles = new String[]{"Home", "Work", "Market", "Gym"};
////
////        String[] labelTitles = new String[]{"Groceries", "Inspiration", "Personal", "Work"};
////        Integer[] labelColors = new Integer[]{R.color.amberDate, R.color.colorPrimary,
////                                            R.color.tealLocation, R.color.app_body_text_1};
////        Integer[] labelQuantities = new Integer[]{0, 1, 2, 3};
////
////        for (int i = 0; i < locationTitles.length; i++)
////            sFavouriteLocations.add(new Location(locationTitles[i], "", 0.0, 0.0, true));
////
////        for (int i = 0; i < 4; i++)
////            sLabels.add(new Label(labelTitles[i], labelColors[i]));
////
////        Random random = new Random();
////        while (amountList > 0) {
////            RealmList<Task> aux = new RealmList<>();
////            int amountTaskWhile = amountTasks;
////
////            while (amountTaskWhile > 0) {
////                Boolean finished = random.nextBoolean();
////                Boolean starred = random.nextBoolean();
////                String title = titles[random.nextInt(2)];
////                String description = descriptions[random.nextInt(3)];
////                Date dueDate = dueDates[random.nextInt(2)];
////                RealmList<Label> labels = new RealmList<>();
////                Integer labelQuantity = labelQuantities[random.nextInt(4)];
////                Location location = sFavouriteLocations.get(random.nextInt(3));
////
////                for (int i = 0; i < labelQuantity; i++)
////                    labels.add(sLabels.get(random.nextInt(4)));
////
////                aux.add(new Task(title, finished, starred, description, dueDate, location, labels));
////                amountTaskWhile--;
////            }
////
////            String listTitle = listTitles[random.nextInt(2)];
////            TaskList taskList = new TaskList(listTitle, aux);
////            sTaskLists.add(taskList);
////            amountList--;
////        }
////
////        Log.d("myApp", "CONTROLLER POPULATED" + " " +
////                sTaskLists.size() + " " +
////                sTaskLists.get(0).getTasks().size() + " " +
////                sLabels.size());
////    }
//
//    // ------------------------ Crud Methods -------------------------
//
////    public void addTask(@NonNull Task newTask){
////
////        // Add on top
////        getTasks().add(0, newTask);
////    }
////
////    public void editTask(int position, @NonNull Task task){
////
////        // Validation of the task title
////        if(!task.getTitle().matches(""))
////            getTasks().set(position, task);
////    }
////
////    public void deleteTask(int position){
////        getTasks().remove(position);
////    }
////
////    public void undoDelete(){
////
////        // Adds the last deleted to the top
////        if(lastDeleted != null)
////            getTasks().add(0, lastDeleted);
////    }
////
////    public void finishTask(int position){
////        actualTask = getTasks().get(position);
////
////        actualTask.setFinished(true);
////        getTasks().set(position, actualTask);
////    }
////
////    public void undoFinished(int position){
////        actualTask = getTasks().get(position);
////
////        actualTask.setFinished(false);
////        getTasks().set(position, actualTask);
////    }
//
//    // -------------------------- Use Cases --------------------------
//
////    public static List<Task> getTasksOnRange(String range, boolean today, boolean last){
////
////        Date min = Calendar.getInstance().getTime();
////        min.setSeconds(0);
////        Date max = new Date();
////
////        switch (range){
////            case "Daily":
////                max.setTime(min.getTime() + Constants.DAY_IN_MILLISECOND);
////                break;
////            case "Weekly":
////                max.setTime(min.getTime() + Constants.WEEK_IN_MILLISECOND);
////                break;
////            case "Minute":
////                max.setTime(min.getTime() + Constants.MINUTE_IN_MILLISECOND);
////                break;
////        }
////
////        // Include today inside the range if requested
////        if(today){
////            min.setMinutes(0);
////            min.setHours(0);
////        }
////
////        // Include the las day of the range if requested
////        if(last){
////            max.setHours(23);
////            max.setMinutes(59);
////            max.setSeconds(59);
////        }
////
////        // Filter the tasks depending on the range
////        List<Task> counter = new ArrayList<>();
////        for (Task task : sTasks) {
////            Date dueDate = task.getDueDate();
////            if(dueDate!=null)
////                if (!task.isFinished() && (dueDate.after(min) && dueDate.before(max) || dueDate.equals(max)))
////                    counter.add(task);
////        }
////
////        return counter;
////    }
//
//    // ---------------------- Getters & Setters ----------------------
//
////    public ArrayList<Task> getTasks(){
////        return sTasks;
////    }
//
////    public void setTasks(ArrayList<Task> tasks) {
////        TaskController.sTasks = tasks;
////    }
//
////    public void setLastDeleted(Task lastDeleted) {
////        this.lastDeleted = lastDeleted;
////    }
//
//}
