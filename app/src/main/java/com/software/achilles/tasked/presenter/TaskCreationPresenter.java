package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

public class TaskCreationPresenter implements Presenter<TaskCreationFragment, TaskCreationPresenter> {

    // --------------------------- Values ----------------------------

    private int mKeyConstant = Constants.ADD_TASK;

    // ------------------------- Attributes --------------------------

    private TaskCreationFragment mFragment;
    private static TaskCreationPresenter instance;

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
//        if (subscription != null) subscription.unsubscribe();
    }
}
