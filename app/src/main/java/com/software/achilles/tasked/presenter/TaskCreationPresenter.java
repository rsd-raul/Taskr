package com.software.achilles.tasked.presenter;

import android.util.Log;
import android.view.View;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class TaskCreationPresenter
        implements Presenter<TaskCreationFragment> {

    // -------------------------- Injected ---------------------------

    private DataManager dataManager;

    // ------------------------- Constructor -------------------------

    @Inject
    public TaskCreationPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    // ------------------------- Attributes --------------------------

    @Inject
    MainPresenter mainPresenter;

    private TaskCreationFragment mFragment;
    private boolean desStatus = false, timStatus = false, locStatus = false,
            labStatus = false, favStatus = false;

//    // -------------------------- Singleton --------------------------
//
//    private static final Object lock = new Object();
//    private static volatile TaskCreationPresenter instance;
//
//    //  Double-checked locking - Effective in Java 1.5 and later:
//    public static TaskCreationPresenter getInstance() {
//        TaskCreationPresenter result = instance;
//
//        // Only synchronize if the TaskCreationPresenter haven't been instantiated
//        if (result == null) {
//            synchronized (lock) {
//                result = instance;
//
//                // If no other threads have instantiated the TaskCreationPresenter while waiting for the lock.
//                if (result == null) {
//                    result = new TaskCreationPresenter();
//                    instance = result;
//                }
//            }
//        }
//        return result;
//    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public void attachView(TaskCreationFragment view) {
        mFragment = view;
//        return instance;
    }

//    public static void destroyPresenter() {
//        if(instance == null)
//            return;
//
//        instance.mFragment = null;
//        instance = null;
//
////      Un-subscribe from the thread?
////        if (subscription != null)
////            subscription.unsubscribe();
//    }

    // ---------------------------- Layout ---------------------------

    public void setupLayout(int listIndex){
        List<String> taskListTitles = new ArrayList<>();

        // Get data for setting the ViewPager
        RealmResults<TaskList> taskList = dataManager.findAllTaskList();

        // Format that data for the spinner
        for (int i = 0; i < taskList.size(); i++)
            taskListTitles.add(taskList.get(i).getTitle());

        // Setup a temporal task
        dataManager.getTemporalTask();

        // Setup the layout
        mFragment.setupLayout(taskListTitles, listIndex);

        mFragment.setupSaveOrVoice(false);
    }

    // -------------------------- Listeners --------------------------

    public void modifiersOnClick(View v){
        int id = v.getId();
        Log.d("AAAAAAAAAAAA", "modifiersOnClick: " + id);
        switch (id){
            case R.id.button_description:
                // Show field

                // If it's OFF, turn ON and vice-versa
                desStatus = !desStatus;
                mFragment.colorModifierButton(id, desStatus);
                break;
            case R.id.button_time:
                // Show picker, then deploy result if any

                // If it's OFF, turn ON and vice-versa
                timStatus = !timStatus;
                mFragment.colorModifierButton(id, timStatus);
                break;
            case R.id.button_location:
                // Show picker, then deploy result if any

                // If it's OFF, turn ON and vice-versa
                locStatus = !locStatus;
                mFragment.colorModifierButton(id, locStatus);
                break;
            case R.id.button_label:
                // Show pop up?, then deploy result and color if any

                // If it's OFF, turn ON and vice-versa
                labStatus = !labStatus;
                mFragment.colorModifierButton(id, labStatus);
                break;
            case R.id.button_favourite:

                // If it's OFF, turn ON and vice-versa
                favStatus = !favStatus;

                // Save
                dataManager.getTemporalTask().setStarred(favStatus);

                mFragment.colorModifierButton(id, favStatus);
                break;
        }
    }

    public boolean isDataPresent(){
        return mFragment.isDataPresent();
    }

    // -------------------------- Use Cases --------------------------

    public void saveTask(boolean reset){

//        mFragment.populateAndGetTemporal();
        Task temporal = mFragment.populateAndGetTemporal();
        int taskListPosition = dataManager.getTemporalTaskListPosition();
        dataManager.saveTask(taskListPosition, temporal);

        Log.d("TaskCreationPresenter", "" +
                dataManager.getTemporalTask().getTitle()
                + " " + dataManager.getTemporalTask().isStarred()
                + " " + dataManager.getTemporalTask().isCompleted()
                + " " + dataManager.getTemporalTask().getNotes()
                + " " + dataManager.getTemporalTask().getDue()
                + " " + dataManager.getTemporalTask().getLabels()
                + " " + dataManager.getTemporalTask().getLocation()
                + " " + dataManager.getTemporalTask().getId()
        );

        dataManager.destroyTemporalTask();

        // If long press, restart all fields, if short, back to Dashboard
        if(reset)
            mFragment.resetFields();
        else
            mainPresenter.backToBack();
    }
}
