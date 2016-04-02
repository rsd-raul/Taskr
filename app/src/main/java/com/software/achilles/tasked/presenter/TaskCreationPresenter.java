package com.software.achilles.tasked.presenter;

import android.content.Context;
import android.view.View;
import com.software.achilles.tasked.R;
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
    private Context mContext;
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
        mContext = view.getContext();
        return instance;
    }

    public static void destroyPresenter() {
        if(instance == null)
            return;

        instance.mFragment = null;
        instance.mContext = null;
        instance = null;

//      Un-subscribe from the thread?
//        if (subscription != null)
//            subscription.unsubscribe();
    }

    // ---------------------------- Layout ---------------------------

    public void setupLayout(){
        List<String> taskListTitles = new ArrayList<>();

        // Get data for setting the ViewPager
        RealmResults<TaskList> taskList = DataManager.getInstance().findAllTaskList();

        // Format that data for the spinner
        for (int i = 0; i < taskList.size(); i++)
            taskListTitles.add(taskList.get(i).getTitle());

        // Setup the layout
        mFragment.setupLayout(taskListTitles);
    }

    // -------------------------- Listeners --------------------------

    public void modifiersOnClick(View v){
        int id = v.getId();
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
                // Save

                // If it's OFF, turn ON and vice-versa
                favStatus = !favStatus;
                mFragment.colorModifierButton(id, favStatus);
                break;
        }
    }
}
