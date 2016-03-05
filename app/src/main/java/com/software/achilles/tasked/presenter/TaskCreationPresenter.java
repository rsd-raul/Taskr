package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.view.MainActivity;

public class TaskCreationPresenter implements Presenter<MainActivity> {

    private MainActivity mActivity;

    @Override
    public void attachView(MainActivity view) {
        mActivity = view;
    }

    @Override
    public void detachView() {
        mActivity = null;
    }

    
}
