package com.software.achilles.tasked.presenter;

import android.util.Log;
import android.view.View;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class TaskCreationPresenter implements Presenter<TaskCreationFragment, TaskCreationPresenter> {

    // --------------------------- Values ----------------------------

    private int mKeyConstant = Constants.ADD_TASK;

    // ------------------------- Attributes --------------------------

    private TaskCreationFragment mFragment;
    private static TaskCreationPresenter instance;
    private boolean desStatus = false, timStatus = false, locStatus = false,
            labStatus = false, favStatus = false;

    // ------------------------- Constructor -------------------------

    public static TaskCreationPresenter getInstance() {
        if(instance == null)
            instance = new TaskCreationPresenter();
        return instance;
    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public TaskCreationPresenter attachView(TaskCreationFragment view) {
        mFragment = view;
        return instance;
    }

    public static void destroyPresenter() {
        if(instance == null)
            return;

        instance.mFragment = null;
        instance = null;

//      Un-subscribe from the thread?
//        if (subscription != null)
//            subscription.unsubscribe();
    }

    // ---------------------------- Layout ---------------------------

    public void setupLayout(int listIndex){
        List<String> taskListTitles = new ArrayList<>();

        // Get data for setting the ViewPager
        RealmResults<TaskList> taskList = DataManager.getInstance().findAllTaskList();

        // Format that data for the spinner
        for (int i = 0; i < taskList.size(); i++)
            taskListTitles.add(taskList.get(i).getTitle());

        // Setup a temporal task
        DataManager.getInstance().getTemporalTask();

        // Setup the layout
        mFragment.setupLayout(taskListTitles, listIndex);
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
                DataManager.getInstance().getTemporalTask().setStarred(favStatus);

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
        int taskListPosition = DataManager.getInstance().getTemporalTaskListPosition();
        DataManager.getInstance().saveTask(taskListPosition, temporal);

        Log.d("TaskCreationPresenter", "" +
                DataManager.getInstance().getTemporalTask().getTitle()
                + " " + DataManager.getInstance().getTemporalTask().isStarred()
                + " " + DataManager.getInstance().getTemporalTask().isCompleted()
                + " " + DataManager.getInstance().getTemporalTask().getNotes()
                + " " + DataManager.getInstance().getTemporalTask().getDue()
                + " " + DataManager.getInstance().getTemporalTask().getLabels()
                + " " + DataManager.getInstance().getTemporalTask().getLocation()
                + " " + DataManager.getInstance().getTemporalTask().getId()
        );

        DataManager.getInstance().destroyTemporalTask();

        // If long press, restart all fields, if short, back to Dashboard
        if(reset)
            mFragment.resetFields();
        else
            MainPresenter.getInstance().backToBack();
    }
}
