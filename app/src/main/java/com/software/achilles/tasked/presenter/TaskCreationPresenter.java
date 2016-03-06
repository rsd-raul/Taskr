package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;

public class TaskCreationPresenter implements Presenter<MainActivity> {

    private int mKeyConstant = Constants.ADD_TASK;
    private MainActivity mActivity;

    public TaskCreationPresenter(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void attachView(MainActivity view) {
        mActivity = view;
    }

    @Override
    public void detachView() {
        mActivity = null;
    }

    
}
