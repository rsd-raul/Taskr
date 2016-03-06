package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

public class TaskCreationPresenter implements Presenter<TaskCreationFragment> {

    private int mKeyConstant = Constants.ADD_TASK;
    private TaskCreationFragment mFragment;

    public TaskCreationPresenter(TaskCreationFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    public void attachView(TaskCreationFragment view) {
        mFragment = view;
    }

    @Override
    public void detachView() {
        mFragment = null;
    }

    
}
