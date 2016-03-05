package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.view.MainActivity;

public class MainPresenter implements Presenter<MainActivity> {

    private MainActivity mActivity;

    @Override
    public void attachView(MainActivity view) {
        mActivity = view;
    }

    @Override
    public void detachView() {
        mActivity = null;

//      Un-subscribe from the thread?
//        if (subscription != null) subscription.unsubscribe();
    }



}
